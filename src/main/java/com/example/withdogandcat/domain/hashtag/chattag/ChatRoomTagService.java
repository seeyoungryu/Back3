package com.example.withdogandcat.domain.hashtag.chattag;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.chat.repo.ChatRoomJpaRepository;
import com.example.withdogandcat.domain.chat.util.ChatRoomMapper;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatRoomTagService {

    private final ChatRoomJpaRepository chatRoomRepository;
    private final ChatRoomTagRepository chatRoomTagRepository;
    private final ChatRoomTagMapRepository chatRoomTagMapRepository;

    private final int MAX_TAGS_PER_ROOM = 3;

    // 태그 추가
    @Transactional
    public ChatRoomTagDto addTagToChatRoom(String roomId, String tagName, Long userId) {
        if (tagName == null || tagName.trim().isEmpty()) {
            throw new BaseException(BaseResponseStatus.ELEMENTS_IS_REQUIRED);
        }

        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

        if (!chatRoom.getCreatorId().getUserId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        long currentTagCount = chatRoomTagMapRepository.countByChatRoom(chatRoom);
        if (currentTagCount >= MAX_TAGS_PER_ROOM) {
            throw new BaseException(BaseResponseStatus.EXCEED_MAX_TAG_LIMIT);
        }

        ChatRoomTag tag = chatRoomTagRepository.findByName(tagName)
                .orElseGet(() -> chatRoomTagRepository.save(new ChatRoomTag(null, tagName)));

        if (chatRoomTagMapRepository.findByChatRoomAndChatRoomTag(chatRoom, tag).isPresent()) {
            throw new BaseException(BaseResponseStatus.ALREADY_EXISTS);
        }

        ChatRoomTagMap chatRoomTagMap = ChatRoomTagMap.builder()
                .chatRoom(chatRoom)
                .chatRoomTag(tag)
                .build();
        chatRoomTagMapRepository.save(chatRoomTagMap);

        return ChatRoomTagDto.from(tag);
    }

    // 특정 채팅방에서 태그 삭제
    @Transactional
    public void removeTagFromChatRoom(String roomId, String tagName, Long requesterUserId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

        ChatRoomTag tag = chatRoomTagRepository.findByName(tagName)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NOT_FOUND));

        ChatRoomTagMap chatRoomTagMap = chatRoomTagMapRepository.findByChatRoomAndChatRoomTag(chatRoom, tag)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_TAG_NOT_FOUND));

        if (!chatRoom.getCreatorId().getUserId().equals(requesterUserId)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        chatRoomTagMapRepository.delete(chatRoomTagMap);

        long count = chatRoomTagMapRepository.countByChatRoomTag(tag);
        if (count == 0) {
            chatRoomTagRepository.delete(tag);
        }
    }

    @Transactional(readOnly = true)
    public List<ChatRoomTagDto> getTagsForChatRoom(String roomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

        return chatRoomTagMapRepository.findByChatRoom(chatRoom).stream()
                .map(chatRoomTagMap -> ChatRoomTagDto.from(chatRoomTagMap.getChatRoomTag()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomDto> getChatRoomsByTag(String tagName) {
        ChatRoomTag tag = chatRoomTagRepository.findByName(tagName)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.TAG_NOT_FOUND));

        return chatRoomTagMapRepository.findByChatRoomTag(tag).stream()
                .map(chatRoomTagMap -> ChatRoomMapper.toDto(chatRoomTagMap.getChatRoom()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomTagDto> getPopularChatRoomTags(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> tagFrequencies = chatRoomTagMapRepository.findChatRoomTagUsageFrequency(pageable);
        return tagFrequencies.stream()
                .map(obj -> new ChatRoomTagDto((Long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatRoomTagDto> getAllTags() {
        List<ChatRoomTag> allTags = chatRoomTagRepository.findAll();
        return allTags.stream()
                .map(ChatRoomTagDto::from)
                .collect(Collectors.toList());
    }
}

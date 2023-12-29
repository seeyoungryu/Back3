package com.example.withdogandcat.domain.chat.hashtag;

import com.example.mailtest.domain.chat.dto.ChatRoomDto;
import com.example.mailtest.domain.chat.entity.ChatRoomEntity;
import com.example.mailtest.domain.chat.repo.ChatRoomJpaRepository;
import com.example.mailtest.domain.chat.util.ChatRoomMapper;
import com.example.mailtest.global.exception.BaseException;
import com.example.mailtest.global.exception.BaseResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final ChatRoomJpaRepository chatRoomRepository;
    private final ChatRoomTagMapRepository chatRoomTagMapRepository;

    private final int MAX_TAGS_PER_ROOM = 3;

    // 태그 추가
    @Transactional
    public TagDto addTagToChatRoom(String roomId, String tagName, Long userId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방 못찾음: " + roomId));

        if (!chatRoom.getCreatorId().getUserId().equals(userId)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        long currentTagCount = chatRoomTagMapRepository.countByChatRoom(chatRoom);
        if (currentTagCount >= MAX_TAGS_PER_ROOM) {
            throw new BaseException(BaseResponseStatus.EXCEED_MAX_TAG_LIMIT);
        }

        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));

        ChatRoomTagMap chatRoomTagMap = ChatRoomTagMap.builder()
                .chatRoom(chatRoom)
                .tag(tag)
                .build();

        chatRoomTagMapRepository.save(chatRoomTagMap);

        return TagDto.from(tag);
    }

    // 새 해시태그 생성 (독립적 태그)
    @Transactional
    public TagDto createTag(TagDto tagDto) {
        // 해시태그 중복 확인
        tagRepository.findByName(tagDto.getName()).ifPresent(t -> {
            throw new IllegalArgumentException("이미 존재하는 해시태그");
        });

        Tag tag = tagRepository.save(new Tag(tagDto.getName()));
        return TagDto.from(tag);
    }

    // 특정 채팅방에서 태그 삭제
    @Transactional
    public void removeTagFromChatRoom(String roomId, String name, Long requesterUserId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방 못찾음"));

        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("태그 못찾음: " + name));

        ChatRoomTagMap chatRoomTagMap = chatRoomTagMapRepository.findByChatRoomAndTag(chatRoom, tag)
                .orElseThrow(() -> new EntityNotFoundException("Tag in ChatRoom not found"));

        if (!chatRoom.getCreatorId().getUserId().equals(requesterUserId)) {
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        chatRoomTagMapRepository.delete(chatRoomTagMap);
    }


    // 모든 태그 조회
    @Transactional(readOnly = true)
    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream().map(this::createTagDto).collect(Collectors.toList());
    }

    // 특정 채팅방의 태그 조회
    @Transactional(readOnly = true)
    public List<TagDto> getTagsForChatRoom(String roomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found"));

        return chatRoomTagMapRepository.findByChatRoom(chatRoom).stream()
                .map(chatRoomTagMap -> new TagDto(chatRoomTagMap.getTag().getId(), chatRoomTagMap.getTag().getName()))
                .collect(Collectors.toList());
    }

    // 특정 해시태그가 포함된 모든 채팅방을 반환하는 메서드
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getChatRoomsByTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new EntityNotFoundException("태그를 찾을 수 없음"));

        return chatRoomTagMapRepository.findByTag(tag).stream()
                .map(chatRoomTagMap -> ChatRoomMapper.toDto(chatRoomTagMap.getChatRoom()))
                .collect(Collectors.toList());
    }

    // 인기 태그 목록을 반환하는 메소드 (상위 10개)
    public List<TagDto> getPopularTags() {
        Pageable limit = PageRequest.of(0, 10);
        List<Object[]> tagFrequencies = tagRepository.findTagUsageFrequency(limit);
        return tagFrequencies.stream()
                .map(obj -> new TagDto((Long) obj[0], (String) obj[1]))
                .collect(Collectors.toList());
    }

    private TagDto createTagDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }

}

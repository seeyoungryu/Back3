package com.example.withdogandcat.domain.chat.service;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDetailDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomListDto;
import com.example.withdogandcat.domain.chat.dto.CreatorDto;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.chat.repo.ChatRoomJpaRepository;
import com.example.withdogandcat.domain.chat.repo.ChatRoomRepository;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private final int MAX_ROOM_COUNT = 50; // 50개의 채팅방을 초과하면 MySQL에 저장하지 않음

    @Transactional
    public BaseResponse<ChatRoomDto> createChatRoom(String name, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .name(name)
                .creatorId(user)
                .roomId(UUID.randomUUID().toString())
                .build();

        chatRoomEntity = chatRoomJpaRepository.save(chatRoomEntity);

        if (chatRoomRepository.count() < MAX_ROOM_COUNT) {
            chatRoomRepository.createChatRoom(chatRoomEntity.getRoomId(), name, user.getUserId());
        }

        ChatRoomDto chatRoomDto = convertToDto(chatRoomEntity);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 생성 성공", chatRoomDto);
    }

    @Transactional
    public BaseResponse<Void> deleteChatRoom(String roomId, String userEmail) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

        if (!chatRoomEntity.getCreatorId().getEmail().equals(userEmail)) {
            throw new BaseException(BaseResponseStatus.AUTHENTICATION_FAILED);
        }

        chatMessageService.deleteMessages(roomId);
        chatRoomRepository.deleteRoom(roomId);
        chatRoomJpaRepository.deleteByRoomId(roomId);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 삭제 성공", null);
    }


    public BaseResponse<List<ChatRoomListDto>> findAllRoomListDtos() {
        List<ChatRoomEntity> chatRoomEntities = chatRoomJpaRepository.findAll();
        List<ChatRoomListDto> chatRoomListDtos = chatRoomEntities.stream()
                .map(this::convertToRoomListDto)
                .collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 목록 조회 성공", chatRoomListDtos);
    }

    public BaseResponse<ChatRoomDetailDto> findRoomDetailById(String roomId) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

        Set<String> memberIds = redisTemplate.opsForSet().members("chatRoom:" + roomId + ":members");
        List<User> members = userRepository.findAllById(
                memberIds.stream().map(Long::parseLong).collect(Collectors.toSet())
        );

        List<CreatorDto> memberDtos = members.stream()
                .map(this::createCreatorDto)
                .collect(Collectors.toList());

        CreatorDto creatorDto = createCreatorDto(chatRoomEntity.getCreatorId());

        ChatRoomDetailDto chatRoomDetailDto = new ChatRoomDetailDto(
                chatRoomEntity.getRoomId(),
                chatRoomEntity.getName(),
                creatorDto,
                memberDtos
        );

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 상세 조회 성공", chatRoomDetailDto);
    }

    /**
     * 중복 메서드 분리
     */
    private ChatRoomListDto convertToRoomListDto(ChatRoomEntity chatRoomEntity) {
        CreatorDto creatorDto = createCreatorDto(chatRoomEntity.getCreatorId());
        return new ChatRoomListDto(
                chatRoomEntity.getRoomId(),
                chatRoomEntity.getName(),
                creatorDto
        );
    }

    private CreatorDto createCreatorDto(User user) {
        return CreatorDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    private ChatRoomDto convertToDto(ChatRoomEntity chatRoomEntity) {
        return ChatRoomDto.builder()
                .id(chatRoomEntity.getId())
                .roomId(chatRoomEntity.getRoomId())
                .name(chatRoomEntity.getName())
                .createdAt(chatRoomEntity.getCreatedAt())
                .creator(createCreatorDto(chatRoomEntity.getCreatorId()))
                .build();
    }
}

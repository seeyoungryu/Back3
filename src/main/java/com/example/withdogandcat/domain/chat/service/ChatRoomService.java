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
    public BaseResponse<ChatRoomEntity> createChatRoom(String name, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .name(name)
                .creatorId(user)
                .roomId(UUID.randomUUID().toString())
                .build();

        chatRoomEntity = chatRoomJpaRepository.save(chatRoomEntity);

        if (chatRoomRepository.count() < MAX_ROOM_COUNT) {
            chatRoomRepository.createChatRoom(chatRoomEntity.getRoomId(), name, user.getUserId());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", chatRoomEntity);
    }

    @Transactional
    public BaseResponse<Void> deleteChatRoom(String roomId, Long userId) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findByRoomId(roomId).orElseThrow();

        if (!chatRoomEntity.getCreatorId().getUserId().equals(userId)) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_TOKEN, "로그인 성공", null);
        }

        chatMessageService.deleteMessages(roomId); // 채팅 메시지 삭제
        chatRoomRepository.deleteRoom(roomId); // Redis에서 채팅방 삭제
        chatRoomJpaRepository.deleteByRoomId(roomId); // MySQL에서 채팅방 삭제

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", null);
    }

    public BaseResponse<List<ChatRoomListDto>> findAllRoomListDtos() {
        List<ChatRoomEntity> chatRoomEntities = chatRoomJpaRepository.findAll();
        List<ChatRoomListDto> chatRoomListDtos = chatRoomEntities.stream()
                .map(this::convertToRoomListDto).collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", chatRoomListDtos);
    }

    private ChatRoomListDto convertToRoomListDto(ChatRoomEntity chatRoomEntity) {
        return new ChatRoomListDto(
                chatRoomEntity.getRoomId(),
                chatRoomEntity.getName(),
                chatRoomEntity.getCreatorId().getNickname()
        );
    }

    public ChatRoomDetailDto findRoomDetailById(String roomId) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findByRoomId(roomId)
                .orElseThrow();

        Set<String> memberIds = redisTemplate.opsForSet().members("chatRoom:" + roomId + ":members");

        List<User> members = userRepository.findAllById(
                memberIds.stream().map(Long::parseLong).collect(Collectors.toSet())
        );

        List<CreatorDto> memberDtos = members.stream()
                .map(user -> new CreatorDto(user.getEmail(), user.getNickname()))
                .collect(Collectors.toList());

        return new ChatRoomDetailDto(
                chatRoomEntity.getRoomId(),
                chatRoomEntity.getName(),
                chatRoomEntity.getCreatorId().getNickname(),
                memberDtos
        );
    }

    public ChatRoomDto convertToDto(ChatRoomEntity chatRoomEntity) {
        CreatorDto creatorDto = CreatorDto.builder()
                .email(chatRoomEntity.getCreatorId().getEmail())
                .nickname(chatRoomEntity.getCreatorId().getNickname())
                .build();

        return ChatRoomDto.builder()
                .id(chatRoomEntity.getId())
                .roomId(chatRoomEntity.getRoomId())
                .name(chatRoomEntity.getName())
                .createdAt(chatRoomEntity.getCreatedAt())
                .creatorId(creatorDto)
                .build();
    }
}

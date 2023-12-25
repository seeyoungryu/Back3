package com.example.withdogandcat.domain.chat.service;

import com.example.withdogandcat.domain.chat.dto.*;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.chat.repo.ChatRoomJpaRepository;
import com.example.withdogandcat.domain.chat.repo.ChatRoomRepository;
import com.example.withdogandcat.domain.pet.PetService;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final PetService petService;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private final int MAX_ROOM_COUNT = 2;

    @Transactional
    public BaseResponse<ChatRoomDto> createChatRoom(String name, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        // 사용자가 이미 생성한 채팅방 수 확인
        long existingRoomsCount = chatRoomJpaRepository.countByCreatorId(user);
        if (existingRoomsCount >= MAX_ROOM_COUNT) {
            throw new BaseException(BaseResponseStatus.EXCEED_MAX_CHATROOM_LIMIT);
        }

        ChatRoomEntity chatRoomEntity = ChatRoomEntity
                .builder()
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

        // Redis에서 채팅방 멤버 목록 삭제
        redisTemplate.delete("chatRoom:" + roomId + ":members");

        // JPA와 Redis에서 채팅방 삭제
        chatMessageService.deleteMessages(roomId);
        chatRoomRepository.deleteRoom(roomId);
        chatRoomJpaRepository.deleteByRoomId(roomId);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 삭제 성공", null);
    }

    /**
     * 사용자가 생성한 채팅방 목록 조회
     */
    public BaseResponse<List<ChatRoomListDto>> findRoomsCreatedByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        List<ChatRoomEntity> userRooms = chatRoomJpaRepository.findByCreatorId(user);
        List<ChatRoomListDto> chatRoomListDtos = userRooms.stream()
                .map(this::convertToRoomListDto)
                .collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용자가 생성한 채팅방 목록 조회 성공", chatRoomListDtos);
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

        // 해당 이메일로 유저 정보 조회
        Set<String> memberEmails = redisTemplate.opsForSet().members("chatRoom:" + roomId + ":members");
        List<String> memberEmailList = new ArrayList<>(memberEmails);
        List<User> members = userRepository.findAllByEmailIn(memberEmailList);


        List<UserInfoDto> memberDtos = members.stream()
                .map(user -> {
                    try {
                        BaseResponse<List<PetResponseDto>> petsResponse = petService.getUserPets(user);
                        List<PetResponseDto> petDtos = petsResponse.getResult();

                        return new UserInfoDto(
                                user.getUserId(),
                                user.getEmail(),
                                user.getNickname(),
                                petDtos
                        );
                    } catch (Exception e) {
                        // 오류 처리: 로그 기록, 기본값 반환 등
                        return new UserInfoDto(user.getUserId(), user.getEmail(), user.getNickname(), null);
                    }
                })
                .collect(Collectors.toList());

        long userCount = chatRoomRepository.getUserCount(roomId);
        ChatRoomDetailDto chatRoomDetailDto = new ChatRoomDetailDto(
                chatRoomEntity.getRoomId(),
                chatRoomEntity.getName(),
                createCreatorDto(chatRoomEntity.getCreatorId()),
                memberDtos,
                userCount
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

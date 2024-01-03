package com.example.withdogandcat.domain.chat.service;

import com.example.withdogandcat.domain.chat.dto.*;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.chat.repo.ChatRoomJpaRepository;
import com.example.withdogandcat.domain.chat.repo.ChatRoomRepository;
import com.example.withdogandcat.domain.chat.util.ChatRoomMapper;
import com.example.withdogandcat.domain.hashtag.chattag.*;
import com.example.withdogandcat.domain.mypage.MyPageService;
import com.example.withdogandcat.domain.pet.PetRepository;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.Pet;
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

    private final PetRepository petRepository;
    private final MyPageService myPageService;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomTagService chatRoomTagService;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ChatRoomTagRepository chatRoomTagRepository;
    private final ChatRoomTagMapRepository chatRoomTagMapRepository;

    private final int MAX_ROOM_COUNT = 2;

    /**
     * 채팅방 생성
     */
    @Transactional
    public BaseResponse<ChatRoomDto> createChatRoom(String name, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        long existingRoomsCount = chatRoomJpaRepository.countByCreatorId(user);
        if (existingRoomsCount >= MAX_ROOM_COUNT) {
            throw new BaseException(BaseResponseStatus.EXCEED_MAX_CHATROOM_LIMIT);
        }

        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .name(name)
                .creatorId(user)
                .roomId(UUID.randomUUID().toString())
                .build();

        chatRoomEntity = chatRoomJpaRepository.save(chatRoomEntity);

        long userActiveRoomsCount = chatRoomRepository.countActiveRoomsByUserId(user.getUserId());
        if (userActiveRoomsCount < MAX_ROOM_COUNT) {
            chatRoomRepository.createChatRoom(chatRoomEntity.getRoomId(), name, user.getUserId());
        }

        List<Pet> pets = petRepository.findByUser(user);
        List<PetResponseDto> petDtos = pets.stream()
                .map(PetResponseDto::from)
                .collect(Collectors.toList());

        ChatRoomDto chatRoomDto = ChatRoomMapper.toDto(chatRoomEntity, petDtos);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 생성 성공", chatRoomDto);
    }


    @Transactional
    public BaseResponse<Void> deleteChatRoom(String roomId, String userEmail) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

        if (!chatRoomEntity.getCreatorId().getEmail().equals(userEmail)) {
            throw new BaseException(BaseResponseStatus.AUTHENTICATION_FAILED);
        }

        List<ChatRoomTagMap> tagMaps = chatRoomTagMapRepository.findByChatRoom(chatRoomEntity);
        for (ChatRoomTagMap tagMap : tagMaps) {
            ChatRoomTag chatRoomTag = tagMap.getChatRoomTag();
            chatRoomTagMapRepository.delete(tagMap);

            long count = chatRoomTagMapRepository.countByChatRoomTag(chatRoomTag);
            if (count == 0) {
                chatRoomTagRepository.delete(chatRoomTag);
            }
        }

        redisTemplate.delete("chatRoom:" + roomId + ":members");
        chatMessageService.deleteMessages(roomId);
        chatRoomRepository.deleteRoom(roomId);
        chatRoomJpaRepository.delete(chatRoomEntity);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 삭제 성공", null);
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<ChatRoomListDto>> findAllRoomListDtos() {
        List<ChatRoomEntity> chatRoomEntities = chatRoomJpaRepository.findAll();
        List<ChatRoomListDto> chatRoomListDtos = chatRoomEntities.stream()
                .map(room -> {
                    List<ChatRoomTagDto> tags = chatRoomTagService.getTagsForChatRoom(room.getRoomId());
                    return ChatRoomMapper.toChatRoomListDto(
                            room, chatMessageService.getLastTalkMessage(room.getRoomId()), tags, petRepository);
                }).collect(Collectors.toList());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 목록 조회 성공", chatRoomListDtos);
    }

    @Transactional(readOnly = true)
    public BaseResponse<ChatRoomDetailDto> findRoomDetailById(String roomId) {
        ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CHATROOM_NOT_FOUND));

        Set<String> memberEmails = redisTemplate.opsForSet().members("chatRoom:" + roomId + ":members");
        List<String> memberEmailList = new ArrayList<>(memberEmails);
        List<User> members = userRepository.findAllByEmailIn(memberEmailList);

        List<UserInfoDto> memberDtos = members.stream()
                .map(user -> {
                    try {
                        BaseResponse<List<PetResponseDto>> petsResponse = myPageService.getUserPets(user);
                        List<PetResponseDto> petDtos = petsResponse.getResult();
                        return new UserInfoDto(
                                user.getUserId(),
                                user.getEmail(),
                                user.getNickname(),
                                petDtos
                        );
                    } catch (Exception e) {
                        return new UserInfoDto(user.getUserId(), user.getEmail(), user.getNickname(), null);
                    }
                }).collect(Collectors.toList());

        ChatRoomDetailDto chatRoomDetailDto = new ChatRoomDetailDto(
                chatRoomEntity.getRoomId(),
                chatRoomEntity.getName(),
                ChatRoomMapper.toCreatorDto(chatRoomEntity.getCreatorId(), petRepository),
                memberDtos
        );

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 상세 조회 성공", chatRoomDetailDto);
    }

}

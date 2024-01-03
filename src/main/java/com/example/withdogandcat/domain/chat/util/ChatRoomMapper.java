package com.example.withdogandcat.domain.chat.util;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomListDto;
import com.example.withdogandcat.domain.chat.dto.CreatorDto;
import com.example.withdogandcat.domain.chat.entity.ChatMessage;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.hashtag.chattag.ChatRoomTagDto;
import com.example.withdogandcat.domain.pet.PetRepository;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomMapper {

    /**
     * 중복메서드를 분리하기 위한 클래스
     */

    public static ChatRoomDto toDto(ChatRoomEntity chatRoomEntity, List<PetResponseDto> petDtos) {
        CreatorDto creatorDto = CreatorDto.builder()
                .userId(chatRoomEntity.getCreatorId().getUserId())
                .email(chatRoomEntity.getCreatorId().getEmail())
                .nickname(chatRoomEntity.getCreatorId().getNickname())
                .pets(petDtos)
                .build();

        return ChatRoomDto.builder()
                .id(chatRoomEntity.getId())
                .roomId(chatRoomEntity.getRoomId())
                .name(chatRoomEntity.getName())
                .createdAt(chatRoomEntity.getCreatedAt())
                .creator(creatorDto)
                .build();
    }

    public static ChatRoomListDto toChatRoomListDto(ChatRoomEntity chatRoomEntity,
                                                    ChatMessage lastTalkMessage,
                                                    List<ChatRoomTagDto> tags,
                                                    PetRepository petRepository) {
        return ChatRoomListDto.builder()
                .roomId(chatRoomEntity.getRoomId())
                .name(chatRoomEntity.getName())
                .creator(toCreatorDto(chatRoomEntity.getCreatorId(), petRepository))
                .lastTalkMessage(lastTalkMessage)
                .tags(tags)
                .build();
    }

    public static CreatorDto toCreatorDto(User user, PetRepository petRepository) {
        List<Pet> pets = petRepository.findByUser(user);
        List<PetResponseDto> petDtos = pets.stream()
                .map(PetResponseDto::from)
                .collect(Collectors.toList());

        return CreatorDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .pets(petDtos)
                .build();
    }
}

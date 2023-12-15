package com.example.withdogandcat.domain.chat.controller;

import com.example.withdogandcat.domain.chat.ChatRoomRepository;
import com.example.withdogandcat.domain.chat.dto.ChatListMessageDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomRequestDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomResponseDto;
import com.example.withdogandcat.domain.chat.model.ChatRoom;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import com.example.withdogandcat.global.common.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 생성
    @PostMapping("/room")
    public ApiResponseDto<ChatRoomResponseDto> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody ChatRoomRequestDto chatRoomRequest) {
        User creator = userDetails.getUser();
        ChatRoom chatRoom = chatRoomRepository.createChatRoom(chatRoomRequest.getName(), creator);
        ChatRoomResponseDto chatRoomDto = ChatRoomResponseDto.from(chatRoom);
        return new ApiResponseDto<>("Chat room created successfully", chatRoomDto);
    }

    // 전체 채팅방 조회
    @GetMapping("/rooms")
    public ApiResponseDto<ChatListMessageDto> getAllRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllRoom();
        List<ChatRoomResponseDto> chatRoomDtos = chatRooms.stream()
                .map(ChatRoomResponseDto::from).collect(Collectors.toList());
        ChatListMessageDto chatListMessageDto = new ChatListMessageDto(chatRoomDtos);
        return new ApiResponseDto<>("Success", chatListMessageDto);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ApiResponseDto<ChatRoomResponseDto> getRoomById(@PathVariable String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findRoomById(roomId);
        if (chatRoom == null) {
            return new ApiResponseDto<>("Chat room not found", null);
        }
        ChatRoomResponseDto chatRoomDto = ChatRoomResponseDto.from(chatRoom);
        return new ApiResponseDto<>("Success", chatRoomDto);
    }

    // 채팅방 삭제
    @DeleteMapping("/room/{roomId}")
    public ApiResponseDto<String> deleteRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @PathVariable String roomId) {
        try {
            User user = userDetails.getUser();
            chatRoomRepository.deleteChatRoom(roomId, user.getUserId());
            return new ApiResponseDto<>("Chat room deleted successfully", roomId);
        } catch (IllegalStateException e) {
            return new ApiResponseDto<>("Error: " + e.getMessage(), null);
        }
    }

    // 특정 채팅방 입장
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "chat/roomdetail";
    }
}

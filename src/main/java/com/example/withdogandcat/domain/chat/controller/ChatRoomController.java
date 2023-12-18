package com.example.withdogandcat.domain.chat.controller;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDetailDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomListDto;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.chat.service.ChatMessageService;
import com.example.withdogandcat.domain.chat.service.ChatRoomService;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @GetMapping("/room")
    public String rooms(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로직 추가 (예: 사용자에 따른 채팅방 목록 가져오기)
        return "/chat/room";
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable("roomId") String roomId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로직 추가 (예: 사용자 권한 확인)
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomListDto> room(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.findAllRoomListDtos().getResult();
    }


    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<ChatRoomDto> createRoom(@RequestParam("name") String name,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatRoomEntity chatRoomEntity = chatRoomService.createChatRoom(name, userDetails.getUser().getUserId()).getResult();
        ChatRoomDto chatRoomDto = chatRoomService.convertToDto(chatRoomEntity);
        return ResponseEntity.ok(chatRoomDto);
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<ChatRoomDetailDto> roomInfo(@PathVariable("roomId") String roomId,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ChatRoomDetailDto response = chatRoomService.findRoomDetailById(roomId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> deleteRoom(@PathVariable("roomId") String roomId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            chatRoomService.deleteChatRoom(roomId, userDetails.getUser().getUserId());
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", "채팅방 삭제 완료"));
        } catch (BaseException e) {
            return ResponseEntity
                    .status(e.getStatus().getCode())
                    .body(new BaseResponse<>(e.getStatus()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(BaseResponseStatus.UNEXPECTED_ERROR.getCode())
                    .body(new BaseResponse<>(BaseResponseStatus.UNEXPECTED_ERROR));
        }
    }

    @GetMapping("/room/{roomId}/messages")
    @ResponseBody
    public ResponseEntity<BaseResponse<List<Object>>> roomMessages(@PathVariable("roomId") String roomId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BaseResponse<List<Object>> response = chatMessageService.getMessages(roomId);
        return ResponseEntity.ok(response);
    }
}

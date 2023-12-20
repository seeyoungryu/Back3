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
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final JwtUtil jwtUtil;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomListDto> room() {
        return chatRoomService.findAllRoomListDtos().getResult();
    }

    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<ChatRoomDto> createRoom(@RequestParam("name") String name, HttpServletRequest request) {

        try {
            String token = jwtUtil.resolveToken(request);
            jwtUtil.validateToken(token);
            String userEmail = jwtUtil.getUserEmailFromToken(token);

            ChatRoomEntity chatRoomEntity = chatRoomService.createChatRoom(name, userEmail).getResult();
            ChatRoomDto chatRoomDto = chatRoomService.convertToDto(chatRoomEntity);

            return ResponseEntity.ok(chatRoomDto);

        } catch (BaseException e) {
            return ResponseEntity
                    .status(e.getStatus().getCode())
                    .body(ChatRoomDto.builder().build());
        }
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<ChatRoomDetailDto> roomInfo(@PathVariable("roomId") String roomId) {
        ChatRoomDetailDto response = chatRoomService.findRoomDetailById(roomId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> deleteRoom(@PathVariable("roomId") String roomId, HttpServletRequest request) {

        try {
            String token = jwtUtil.resolveToken(request);
            jwtUtil.validateToken(token);
            String userEmail = jwtUtil.getUserEmailFromToken(token);

            chatRoomService.deleteChatRoom(roomId, userEmail);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 삭제 완료", null));

        } catch (BaseException e) {
            return ResponseEntity
                    .status(e.getStatus().getCode())
                    .body(new BaseResponse<>(e.getStatus(), e.getMessage(), null));

        } catch (Exception e) {
            return ResponseEntity
                    .status(BaseResponseStatus.UNEXPECTED_ERROR.getCode())
                    .body(new BaseResponse<>(BaseResponseStatus.UNEXPECTED_ERROR, e.getMessage(), null));
        }
    }

    @GetMapping("/room/{roomId}/messages")
    @ResponseBody
    public ResponseEntity<BaseResponse<List<Object>>> roomMessages(@PathVariable("roomId") String roomId) {
        BaseResponse<List<Object>> response = chatMessageService.getMessages(roomId);
        return ResponseEntity.ok(response);
    }

}

package com.example.withdogandcat.domain.chat.controller;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDetailDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.domain.chat.dto.ChatRoomListDto;
import com.example.withdogandcat.domain.chat.repo.ChatRoomRepository;
import com.example.withdogandcat.domain.chat.service.ChatMessageService;
import com.example.withdogandcat.domain.chat.service.ChatRoomService;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 전체 조회
     */
    @GetMapping("/rooms")
    @ResponseBody
    public ResponseEntity<BaseResponse<List<ChatRoomListDto>>> room() {
        BaseResponse<List<ChatRoomListDto>> response = chatRoomService.findAllRoomListDtos();
        return ResponseEntity.ok(response);
    }

    /**
     * 채팅방 생성
     */
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<BaseResponse<ChatRoomDto>> createRoom(@RequestParam("name") String name,
                                                                HttpServletRequest request) {
        try {
            String token = jwtUtil.resolveToken(request);
            jwtUtil.validateToken(token, false);
            String userEmail = jwtUtil.getUserEmailFromToken(token);

            BaseResponse<ChatRoomDto> response = chatRoomService.createChatRoom(name, userEmail);
            return ResponseEntity.ok(response);

        } catch (BaseException e) {
            return ResponseEntity
                    .status(e.getStatus().getCode())
                    .body(new BaseResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    /**
     * 특정 채팅방 조회
     */
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<BaseResponse<ChatRoomDetailDto>> roomInfo(@PathVariable("roomId") String roomId) {
        ChatRoomDetailDto chatRoomDetail = chatRoomService.findRoomDetailById(roomId).getResult();
        long userCount = chatRoomRepository.getUserCount(roomId);
        chatRoomDetail.setUserCount(userCount);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 상세 조회 성공", chatRoomDetail));
    }

    /**
     * 특정 채팅방 삭제
     */
    @DeleteMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<BaseResponse<String>> deleteRoom(@PathVariable("roomId") String roomId,
                                                           HttpServletRequest request) {
        try {
            String token = jwtUtil.resolveToken(request);
            jwtUtil.validateToken(token, false);
            String userEmail = jwtUtil.getUserEmailFromToken(token);

            chatRoomService.deleteChatRoom(roomId, userEmail);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "채팅방 삭제 완료", null));

        } catch (BaseException e) {
            return ResponseEntity.status(e.getStatus().getCode())
                    .body(new BaseResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    /**
     * 특정 채팅방의 메세지 조회
     */
    @GetMapping("/room/{roomId}/messages")
    @ResponseBody
    public ResponseEntity<BaseResponse<List<Object>>> roomMessages(@PathVariable("roomId") String roomId) {
        BaseResponse<List<Object>> response = chatMessageService.getMessages(roomId);
        return ResponseEntity.ok(response);
    }

}

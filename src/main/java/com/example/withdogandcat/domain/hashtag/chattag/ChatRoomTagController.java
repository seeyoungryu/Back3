package com.example.withdogandcat.domain.hashtag.chattag;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class ChatRoomTagController {

    private final ChatRoomTagService chatRoomTagService;

    /**
     * 태그 등록
     */
    @PostMapping("/chatrooms/{roomId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public BaseResponse<List<ChatRoomTagDto>> addTagToChatRoom(@PathVariable("roomId") String roomId,
                                                               @Validated @RequestBody List<String> tags,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatRoomTagDto> addedTags = chatRoomTagService.addTagToChatRoom(roomId, tags, userDetails.getUser().getUserId());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "태그가 추가되었습니다.", addedTags);
    }

    /**
     * 특정 태그 등록한 모든 채팅방 조회
     */
    @GetMapping("/chatrooms/{tagName}")
    public BaseResponse<List<ChatRoomDto>> getChatRoomsByTag(@PathVariable("tagName") String tagName) {
        List<ChatRoomDto> chatRooms = chatRoomTagService.getChatRoomsByTag(tagName);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "조회 성공", chatRooms);
    }

    /**
     * 태그 삭제
     */
    @DeleteMapping("/chatrooms/{roomId}/{tagName}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public BaseResponse<Void> removeTagFromChatRoom(@PathVariable("roomId") String roomId,
                                                    @PathVariable("tagName") String tagName,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomTagService.removeTagFromChatRoom(roomId, tagName, userDetails.getUser().getUserId());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    /**
     * 모든 태그 조회
     */
    @GetMapping("/chatrooms")
    public BaseResponse<List<ChatRoomTagDto>> getAllTags() {
        List<ChatRoomTagDto> allTags = chatRoomTagService.getAllTags();
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "모든 태그 조회 성공", allTags);
    }

    /**
     * 인기 태그 조회
     */
    @GetMapping("/chatrooms/popular")
    public BaseResponse<List<ChatRoomTagDto>> getPopularTags() {
        List<ChatRoomTagDto> popularTags = chatRoomTagService.getPopularChatRoomTags(7);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "인기 태그 조회 성공", popularTags);
    }

}

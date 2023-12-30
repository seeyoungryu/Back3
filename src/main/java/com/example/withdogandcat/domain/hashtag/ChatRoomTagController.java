package com.example.withdogandcat.domain.hashtag;

import com.example.withdogandcat.domain.chat.dto.ChatRoomDto;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class ChatRoomTagController {

    private final TagService tagService;
    private final ChatRoomTagService chatRoomTagService;

    @PostMapping("/chatrooms/{roomId}")
    public BaseResponse<List<TagDto>> addTagToChatRoom(@PathVariable("roomId") String roomId,
                                                       @RequestBody List<String> tags,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<TagDto> addedTags = tags.stream()
                    .map(tag -> chatRoomTagService.addTagToChatRoom(roomId, tag, userDetails.getUser().getUserId()))
                    .map(tag -> TagDto.from(Tag.from(tag)))
                    .collect(Collectors.toList());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "태그가 추가되었습니다.", addedTags);
        } catch (EntityNotFoundException e) {
            return new BaseResponse<>(BaseResponseStatus.CHATROOM_NOT_FOUND);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    @GetMapping("/chatrooms/{tagName}")
    public BaseResponse<List<ChatRoomDto>> getChatRoomsByTag(@PathVariable("tagName") String tagName) {
        try {
            List<ChatRoomDto> chatRooms = chatRoomTagService.getChatRoomsByTag(tagName);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "조회 성공", chatRooms);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    @DeleteMapping("/chatrooms/{roomId}/{tagName}")
    public BaseResponse<Void> removeTagFromChatRoom(@PathVariable("roomId") String roomId,
                                                    @PathVariable("tagName") String tagName,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            chatRoomTagService.removeTagFromChatRoom(roomId, tagName, userDetails.getUser().getUserId());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (EntityNotFoundException e) {
            return new BaseResponse<>(BaseResponseStatus.CHATROOM_NOT_FOUND);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    @GetMapping("/chatrooms")
    public BaseResponse<List<TagDto>> getAllTags() {
        try {
            List<TagDto> allTags = tagService.getAllTags();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "모든 태그 조회 성공", allTags);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    @GetMapping("/chatrooms/popular")
    public BaseResponse<List<TagDto>> getPopularTags() {
        try {
            List<TagDto> popularTags = tagService.getPopularChatRoomTags(5);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "인기 태그 조회 성공", popularTags);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

}

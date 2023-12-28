package com.example.withdogandcat.domain.chat.hashtag;

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
public class TagController {

    private final TagService tagService;

    // 채팅방에 태그 추가
    @PostMapping("/chatrooms/{roomId}")
    public BaseResponse<List<TagDto>> addTagToChatRoom(@PathVariable("roomId") String roomId,
                                                       @RequestBody List<String> tags,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<TagDto> addedTags = tags.stream()
                    .map(tag -> tagService.addTagToChatRoom(roomId, tag, userDetails.getUser().getUserId()))
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

    // 특정 해시태그가 포함된 모든 채팅방 조회
    @GetMapping("/chatrooms/bytag/{tagName}")
    public BaseResponse<List<ChatRoomDto>> getChatRoomsByTag(@PathVariable("tagName") String tagName) {
        try {
            List<ChatRoomDto> chatRooms = tagService.getChatRoomsByTag(tagName);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "조회 성공", chatRooms);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    // 새 해시태그 생성
    @PostMapping
    public BaseResponse<TagDto> createTag(@RequestParam String tagName) {
        try {
            TagDto createdTag = tagService.createTag(new TagDto(tagName));
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "새 태그가 생성되었습니다.", createdTag);
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(BaseResponseStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    // 특정 채팅방에서 태그 삭제
    @DeleteMapping("/chatrooms/{roomId}/tags/{name}")
    public BaseResponse<Void> removeTagFromChatRoom(@PathVariable("roomId") String roomId,
                                                    @PathVariable("name") String name,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            tagService.removeTagFromChatRoom(roomId, name, userDetails.getUser().getUserId());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (EntityNotFoundException e) {
            return new BaseResponse<>(BaseResponseStatus.CHATROOM_NOT_FOUND);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    // 모든 해시태그 조회
    @GetMapping
    public BaseResponse<List<TagDto>> getAllTags() {
        try {
            List<TagDto> allTags = tagService.getAllTags();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "모든 태그 조회 성공", allTags);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    // 인기 태그를 반환하는 API 엔드포인트
    @GetMapping("/popular")
    public BaseResponse<List<TagDto>> getPopularTags() {
        try {
            List<TagDto> popularTags = tagService.getPopularTags();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "인기 태그 조회 성공", popularTags);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

}

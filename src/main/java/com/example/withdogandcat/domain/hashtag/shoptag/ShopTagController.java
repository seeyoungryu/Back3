package com.example.withdogandcat.domain.hashtag.shoptag;

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
public class ShopTagController {

    private final ShopTagService shopTagService;

    /**
     * 가게 태그 추가
     */
    @PostMapping("/shops/{shopId}")
    public BaseResponse<List<ShopTagDto>> addTagToShop(@PathVariable("shopId") Long shopId,
                                                       @RequestBody List<String> tags,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<ShopTagDto> addedTags = tags.stream()
                    .map(tag -> shopTagService.addTagToShop(shopId, tag, userDetails.getUser().getUserId()))
                    .map(tag -> ShopTagDto.from(ShopTag.from(tag)))
                    .collect(Collectors.toList());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "태그가 추가되었습니다.", addedTags);
        } catch (EntityNotFoundException e) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    /**
     * 태그로 가게 조회
     */
    @GetMapping("/shops/{tagName}")
    public BaseResponse<List<ShopTagDto>> getShopsByTag(@PathVariable("tagName") String tagName) {
        try {
            List<ShopTagDto> shops = shopTagService.getShopsByTag(tagName);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "조회 성공", shops);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    /**
     * 가게 태그 삭제
     */
    @DeleteMapping("/shops/{shopId}/{tagName}")
    public BaseResponse<Void> removeTagFromShop(@PathVariable("shopId") Long shopId,
                                                @PathVariable("tagName") String tagName,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            shopTagService.removeTagFromShop(shopId, tagName, userDetails.getUser().getUserId());
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch (EntityNotFoundException e) {
            return new BaseResponse<>(BaseResponseStatus.SHOP_NOT_FOUND);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    /**
     * 모든 태그 조회
     */
    @GetMapping("/shops")
    public BaseResponse<List<ShopTagDto>> getAllTags() {
        try {
            List<ShopTagDto> allTags = shopTagService.getAllTags();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "모든 태그 조회 성공", allTags);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

    /**
     * 인기 태그 조회
     */
    @GetMapping("/shops/popular")
    public BaseResponse<List<ShopTagDto>> getPopularTags() {
        try {
            List<ShopTagDto> popularTags = shopTagService.getPopularShopTags(5);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "인기 태그 조회 성공", popularTags);
        } catch (Exception e) {
            return new BaseResponse<>(BaseResponseStatus.SERVER_ERROR);
        }
    }

}

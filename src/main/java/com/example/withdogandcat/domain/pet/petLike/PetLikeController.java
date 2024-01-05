package com.example.withdogandcat.domain.pet.petLike;

import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets/{petId}")
public class PetLikeController {

    private final PetLikeService petLikeService;

    @PostMapping("/like")
    public ResponseEntity<BaseResponse<Void>> addLike(@PathVariable("petId") Long petId,
                                                      Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        petLikeService.createPetLike(petId, userId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "좋아요 추가 성공", null));
    }

    @GetMapping("/like")
    public ResponseEntity<BaseResponse<Boolean>> checkPetLikeStatus(@PathVariable("petId") Long petId,
                                                                    Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        boolean isPetLiked = petLikeService.isPetLiked(userId, petId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공",isPetLiked));
    }

    @DeleteMapping("/unlike")
    public ResponseEntity<BaseResponse<Void>> unlike(@PathVariable("petId") Long petId,
                                                     Authentication authentication) {
        Long userId = ((UserDetailsImpl) authentication.getPrincipal()).getUser().getUserId();
        petLikeService.deletePetLike(petId, userId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "좋아요 삭제 성공", null));
    }

}

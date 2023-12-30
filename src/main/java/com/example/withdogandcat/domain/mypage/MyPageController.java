package com.example.withdogandcat.domain.mypage;

import com.example.withdogandcat.domain.chat.dto.ChatRoomListDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.shop.dto.ShopResponseDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import com.example.withdogandcat.global.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final JwtUtil jwtUtil;
    private final MyPageService myPageService;

    @GetMapping("/api/shops/mypage")
    public ResponseEntity<BaseResponse<List<ShopResponseDto>>> getShopsByCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        List<ShopResponseDto> shops = myPageService.getShopsByCurrentUser(currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", shops));
    }

    @GetMapping("/api/pets/mypage")
    public ResponseEntity<BaseResponse<List<PetResponseDto>>> getUserPets(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        List<PetResponseDto> pets = myPageService.getUserPets(currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
    }

    @GetMapping("/chat/mypage")
    @ResponseBody
    public ResponseEntity<BaseResponse<List<ChatRoomListDto>>> myRooms(HttpServletRequest request) {

        try {
            String token = jwtUtil.resolveToken(request);
            jwtUtil.validateToken(token, false);
            String userEmail = jwtUtil.getUserEmailFromToken(token);

            List<ChatRoomListDto> userRooms = myPageService.findRoomsCreatedByUser(userEmail).getResult();
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용자 채팅방 조회 성공", userRooms));

        } catch (BaseException e) {
            return ResponseEntity.status(e.getStatus().getCode())
                    .body(new BaseResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }
}

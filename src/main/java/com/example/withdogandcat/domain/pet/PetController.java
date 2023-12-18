package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import com.example.withdogandcat.global.common.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;


    // 마이페이지 반려동물 조회
    @GetMapping("/mypage")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<List<PetResponseDto>>> getUserPets(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        List<PetResponseDto> pets = petService.getUserPets(currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", pets));
    }

    // 반려동물 등록
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<PetResponseDto>> createPet(
            @ModelAttribute PetRequestDto petRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto createdPet = petService.createPet(petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", createdPet));
    }

    // 반려동물 전체조회
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PetResponseDto>>> getAllPets() {
        List<PetResponseDto> pets = petService.getAllPets().getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", pets));
    }

    // 반려동물 상세조회
    @GetMapping("/{petId}")
    public ResponseEntity<BaseResponse<PetResponseDto>> getPet(@PathVariable("petId") Long petId) {
        PetResponseDto petResponseDto = petService.getPet(petId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", petResponseDto));
    }

    // 반려동물 수정
    @PutMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<PetResponseDto>> updatePet(
            @PathVariable("petId") Long petId,
            @ModelAttribute PetRequestDto petRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto updatedPet = petService.updatePet(petId, petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", updatedPet));
    }

    // 반려동물 삭제
    @DeleteMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<Void>> deletePet(@PathVariable("petId") Long petId) {
        petService.deletePet(petId);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "로그인 성공", null));
    }

}

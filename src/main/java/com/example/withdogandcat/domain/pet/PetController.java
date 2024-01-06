package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.common.LoginAccount;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    /**
     * 반려동물 등록
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<PetResponseDto>> createPet(
            @Valid @ModelAttribute PetRequestDto petRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto createdPet = petService.createPet(petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", createdPet));
    }

    /**
     * 모든 반려동물 조회
     */
    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PetResponseDto>>> getAllPets() {
        List<PetResponseDto> pets = petService.getAllPets().getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
    }

    /**
     * 특정 반려동물 조회
     */
    @GetMapping("/{petId}")
    public ResponseEntity<BaseResponse<PetResponseDto>> getPet(@PathVariable("petId") Long petId) {
        PetResponseDto petResponseDto = petService.getPet(petId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", petResponseDto));
    }

    /**
     * 반려동물 수정
     */
    @PutMapping("/{petId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<PetResponseDto>> updatePet(
            @PathVariable("petId") Long petId,
            @Valid @ModelAttribute PetRequestDto petRequestDto,
            @RequestParam(value = "imageUrl", required = false) List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto updatedPet = petService.updatePet(petId, petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", updatedPet));
    }

    /**
     * 반려동물 삭제
     */
    @DeleteMapping("/{petId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> deletePet(@PathVariable Long petId,
                                                        @LoginAccount User currentUser) {
        petService.deletePet(petId, currentUser);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }

}

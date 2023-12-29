package com.example.withdogandcat.domain.pet;

import com.example.mailtest.domain.pet.dto.PetRequestDto;
import com.example.mailtest.domain.pet.dto.PetResponseDto;
import com.example.mailtest.domain.user.entity.User;
import com.example.mailtest.global.common.BaseResponse;
import com.example.mailtest.global.common.LoginAccount;
import com.example.mailtest.global.exception.BaseResponseStatus;
import com.example.mailtest.global.security.impl.UserDetailsImpl;
import jakarta.validation.Valid;
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


    @GetMapping("/mypage")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<List<PetResponseDto>>> getUserPets(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        List<PetResponseDto> pets = petService.getUserPets(currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<PetResponseDto>> createPet(
            @Valid @ModelAttribute PetRequestDto petRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto createdPet = petService.createPet(petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", createdPet));
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PetResponseDto>>> getAllPets() {
        List<PetResponseDto> pets = petService.getAllPets().getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<BaseResponse<PetResponseDto>> getPet(@PathVariable("petId") Long petId) {
        PetResponseDto petResponseDto = petService.getPet(petId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", petResponseDto));
    }

    @PutMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<PetResponseDto>> updatePet(
            @PathVariable("petId") Long petId,
            @Valid @ModelAttribute PetRequestDto petRequestDto,
            @RequestParam(value = "imageUrl", required = false) List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto updatedPet = petService.updatePet(petId, petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", updatedPet));
    }


    @DeleteMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<BaseResponse<Void>> deletePet(@PathVariable Long petId, @LoginAccount User currentUser) {
        petService.deletePet(petId, currentUser);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }
}


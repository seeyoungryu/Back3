package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.tool.ApiResponseDto;
import com.example.withdogandcat.global.tool.LoginAccount;
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

    //반려동물 등록
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ApiResponseDto<PetResponseDto>> createPet(
            @ModelAttribute PetRequestDto petRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto createdPet = petService.createPet(petRequestDto, imageFiles, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("왈왈이 등록 성공", createdPet));
    }


    //반려동물 전체조회
    @GetMapping("")
    public ResponseEntity<ApiResponseDto<List<PetResponseDto>>> getAllPets() {
        ApiResponseDto<List<PetResponseDto>> response = petService.getAllPets();
        return ResponseEntity.ok(response);
    }


    //반려동물 상세조회
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDto> getPet(@PathVariable("petId") Long petId) {
        PetResponseDto petResponseDto = petService.getPet(petId);
        return ResponseEntity.ok(petResponseDto);
    }




    //반려동물 수정
    @PutMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable("petId") Long petId,
            @ModelAttribute PetRequestDto petRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto updatedPet = petService.updatePet(petId, petRequestDto, imageFiles, currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPet);
    }

    //

    //반려동물 삭제
    @DeleteMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> deletePet(@PathVariable("petId") Long petId, @LoginAccount User currentUser) {
        petService.deletePet(petId, currentUser);
        return ResponseEntity.noContent().build();
    }
}

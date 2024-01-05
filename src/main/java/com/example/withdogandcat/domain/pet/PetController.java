package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.common.LoginAccount;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import com.example.withdogandcat.global.security.impl.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    @PostMapping("")
    public ResponseEntity<BaseResponse<PetResponseDto>> createPet(
            @Valid @ModelAttribute PetRequestDto petRequestDto,
            @RequestPart("imageUrl") List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto createdPet = petService.createPet(petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", createdPet));
    }


    //nextpage 반영 전 코드
//    @GetMapping("")
//    public ResponseEntity<BaseResponse<Page<PetResponseDto>>> getAllPets(Pageable pageable) {
//        Page<PetResponseDto> pets = petService.getAllPetsSortedByPetLikes(pageable).getResult();
//        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
//    }



//    //페이지네이션 (프론트 요구사항:nextpage 반영)
//    @GetMapping("")
//    public ResponseEntity<BaseResponse<Map<String, Object>>> getAllPets(Pageable pageable) {
//        Map<String, Object> pets = petService.getAllPetsSortedByPetLikes(pageable).getResult();
//        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
//    }



    //프론트 추가 요구사항 반영
    @GetMapping("")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getAllPets(
            @RequestParam(required = false) Long lastPetId,
            @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> pets = petService.getAllPetsSortedByPetLikes(lastPetId, limit).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
    }


    @GetMapping("/{petId}")
    public ResponseEntity<BaseResponse<PetResponseDto>> getPet(@PathVariable("petId") Long petId) {
        PetResponseDto petResponseDto = petService.getPet(petId).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", petResponseDto));
    }

    @PutMapping("/{petId}")
    public ResponseEntity<BaseResponse<PetResponseDto>> updatePet(
            @PathVariable("petId") Long petId,
            @Valid @ModelAttribute PetRequestDto petRequestDto,
            @RequestParam(value = "imageUrl", required = false) List<MultipartFile> imageFiles,
            @LoginAccount User currentUser) throws IOException {

        PetResponseDto updatedPet = petService.updatePet(petId, petRequestDto, imageFiles, currentUser).getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", updatedPet));
    }


    @DeleteMapping("/{petId}")
    public ResponseEntity<BaseResponse<Void>> deletePet(@PathVariable Long petId, @LoginAccount User currentUser) {
        petService.deletePet(petId, currentUser);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", null));
    }


    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<PetResponseDto>>> getAllPetsWithoutLikes() {
        List<PetResponseDto> pets = petService.getAllPetsWithoutLikes().getResult();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "성공", pets));
    }

}

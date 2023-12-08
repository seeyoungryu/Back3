package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import com.example.withdogandcat.domain.pet.entity.PetGender;
import com.example.withdogandcat.domain.pet.entity.PetKind;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.tool.LoginAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


    //반려동물 등록
    @PostMapping("")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<PetResponseDto> registerPet(
            @RequestParam("petName") String petName,
            @RequestParam("petGender") PetGender petGender,
            @RequestParam("petKind") PetKind petKind,
            @RequestParam("petInfo") String petInfo,
            @RequestParam("imageUrl") MultipartFile image,
            @LoginAccount User currentUser) throws IOException {

        PetRequestDto petRequestDto = new PetRequestDto(petName, petGender, petKind, petInfo, image);
        PetResponseDto registeredPet = petService.registerPet(petRequestDto, image, currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(registeredPet);
    }



    //반려동물 전체조회
    @GetMapping("")
    public ResponseEntity<List<PetResponseDto>> getAllPets() {
        List<PetResponseDto> responseBody = petService.getAllPets();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }


    //반려동물 상세조회
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDto> getPet(@PathVariable Long petId) {
        PetResponseDto petResponseDto = petService.getPet(petId);
        return ResponseEntity.ok(petResponseDto);
    }



    //반려동물 삭제
    @DeleteMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }

}

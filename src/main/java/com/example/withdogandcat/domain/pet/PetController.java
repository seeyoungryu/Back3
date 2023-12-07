package com.example.withdogandcat.domain.pet;

import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.pet.dto.PetResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user/{id}/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponseDto> registerPet(
            @PathVariable(value = "userId") Long userId,
            @RequestParam("petName") String petName,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageUrl) throws IOException {

        PetRequestDto requestDto = new PetRequestDto(petName, imageUrl);
        PetResponseDto responseDto = petService.registerPet(userId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    // 애완동물 정보 조회
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDto> getPet(
            @PathVariable("userId") Long userId,
            @PathVariable("petId") Long petId) {
        PetResponseDto petResponseDto = petService.getPet(userId, petId);

        if (petResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(petResponseDto);
    }

    @PutMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable("userId") Long userId,
            @PathVariable("petId") Long petId,
            @RequestParam("petName") String petName,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageUrl) throws IOException {

        PetRequestDto requestDto = new PetRequestDto(petName, imageUrl);

        PetResponseDto responseDto = petService.updatePet(petId, requestDto);

        return ResponseEntity.ok(responseDto);
    }
}




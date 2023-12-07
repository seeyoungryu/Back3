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
            @PathVariable(value = "id") Long userId,
            @RequestParam("petName") String petName,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageUrl) throws IOException {

        PetRequestDto requestDto = new PetRequestDto(petName, imageUrl); // 여기서 DTO를 수동으로 생성합니다.
        PetResponseDto responseDto = petService.registerPet(userId, requestDto); // DTO를 서비스 메서드에 전달합니다.
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    // 애완동물 정보 조회
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponseDto> getPet(
            @PathVariable("id") Long userId,
            @PathVariable("petId") Long petId) {
        PetResponseDto petResponseDto = petService.getPet(userId, petId);

        if (petResponseDto == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found 반환
        }
        return ResponseEntity.ok(petResponseDto); // 200 OK 반환
    }

    @PutMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponseDto> updatePet(
            @PathVariable("id") Long userId,
            @PathVariable("petId") Long petId,
            @RequestParam("petName") String petName,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageUrl) throws IOException {

        // PetRequestDto 객체를 생성합니다.
        PetRequestDto requestDto = new PetRequestDto(petName, imageUrl);

        // 서비스 메서드를 호출하여 PetResponseDto 객체를 받습니다.
        PetResponseDto responseDto = petService.updatePet(petId, requestDto);

        // 성공적으로 업데이트되었다면, HTTP 200 OK 상태 코드와 함께 업데이트된 데이터를 반환합니다.
        return ResponseEntity.ok(responseDto);
    }
}




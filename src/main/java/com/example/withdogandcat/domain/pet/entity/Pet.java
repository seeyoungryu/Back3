package com.example.withdogandcat.domain.pet.entity;
import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "pets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;                                             

    private String petName;
    private String petInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender petGender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetKind petKind;

    private String imageUrl;


    @Builder
    public Pet(User user, String petName, PetGender petGender,
               PetKind petKind, String petInfo,
               String imageUrl) {
        this.user = user;
        this.petName = petName;
        this.petGender = petGender;
        this.petKind = petKind;
        this.imageUrl = imageUrl;
        this.petInfo = petInfo;
    }


    public static Pet of(PetRequestDto petRequestDto, String imageUrl, User user) {
        return Pet.builder()
                .petKind(petRequestDto.getPetKind())
                .petGender(petRequestDto.getPetGender())
                .petInfo(petRequestDto.getPetInfo())
                .petName(petRequestDto.getPetName())
                .user(user)
                .imageUrl(imageUrl)
                .build();
    }

    public void updatePetKind(PetKind petKind) {
        this.petKind = petKind;

    }
    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateName(String petName) {
        this.petName = petName;
    }

    public void updatePetInfo(String petInfo) {
        this.petInfo = petInfo;
    }

    public void updatePetGender(PetGender petGender) {
        this.petGender = petGender;
    }

}




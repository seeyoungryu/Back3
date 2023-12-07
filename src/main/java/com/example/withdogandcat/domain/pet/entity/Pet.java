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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetKind kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String imageName;

    @Column(length = 500) // petInfo 필드의 길이를 지정합니다.
    private String petInfo;

    @Builder
    public Pet(User user, String name, PetGender petGender, PetKind petKind, String imageUrl, String imageName, String petInfo) {
        this.user = user;
        this.name = name;
        this.gender = petGender;
        this.kind = petKind;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
        this.petInfo = petInfo;
    }


    public static Pet createPet(PetRequestDto requestDto, User user, String imageUrl, String imageName) {
        return Pet.builder()
                .user(user)
                .name(requestDto.getPetName())
                .imageUrl(imageUrl)
                .imageName(imageName)
                .petInfo(requestDto.getPetInfo())
                .petGender(requestDto.getPetGender())
                .petKind(requestDto.getPetKind())
                .build();
    }


    public void updateImage(String imageUrl, String originalFilename) {
        this.imageUrl = imageUrl;
        this.imageName = originalFilename;

    }

    public void updateName(String petName) {
        this.name = petName;
    }


    public void updatePetInfo(String petInfo) {
        this.petInfo = petInfo;
    }

    public void updatePetGender(PetGender gender) {
        this.gender = gender;
    }

    public void updatePetKind(PetKind kind) {
        this.kind = kind;
    }
}
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

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private String imageName;

    @Builder
    public Pet(User user, String name, String imageUrl, String imageName) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public static Pet createPet(PetRequestDto requestDto, User user, String imageUrl, String imageName) {
        return Pet.builder()
                .name(requestDto.getPetName())
                .user(user)
                .imageUrl(imageUrl)
                .imageName(imageName)
                .build();
    }

    public void updateImage(String imageUrl, String originalFilename) {
        this.imageUrl = imageUrl;
        this.imageName = originalFilename;

    }

    public void updateName(String petName) {
        this.name = petName;
    }
}
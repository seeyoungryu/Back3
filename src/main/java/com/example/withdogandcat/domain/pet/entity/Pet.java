package com.example.withdogandcat.domain.pet.entity;
import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.pet.dto.PetRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();


    @Builder
    public Pet(User user, String petName, PetGender petGender,
               PetKind petKind, String petInfo,
               List<Image> images) {
        this.user = user;
        this.petName = petName;
        this.petGender = petGender;
        this.petKind = petKind;
        this.petInfo = petInfo;
        this.images.addAll(images);
    }


    public static Pet of(PetRequestDto petRequestDto, User user) {
        return Pet.builder()
                .petKind(petRequestDto.getPetKind())
                .petGender(petRequestDto.getPetGender())
                .petInfo(petRequestDto.getPetInfo())
                .petName(petRequestDto.getPetName())
                .user(user)
                .images(new ArrayList<>())
                .build();
    }


    public void updatePetDetails(String petName, String petInfo,
                                 PetKind petKind, PetGender petGender) {
        this.petName = petName;
        this.petKind = petKind;
        this.petInfo = petInfo;
        this.petGender = petGender;

    }

    public void addImage(Image image) {
        images.add(image);
        image.setPet(this);
    }

    public void clearImages() {this.images.clear();}

}




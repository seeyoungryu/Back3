//package com.example.withdogandcat.domain.pet.petLike;
//
//import com.example.withdogandcat.domain.pet.entity.Pet;
//import com.example.withdogandcat.domain.user.entity.User;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@Table(name = "pet_likes")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class PetLike {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "pet_id")
//    private Pet pet;
//
//
//    public PetLike(User user, Pet pet) {
//        this.user = user;
//        this.pet = pet;
//    }
//}
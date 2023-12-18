package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.image.ImageRepository;
import com.example.withdogandcat.domain.image.ImageS3Service;
import com.example.withdogandcat.domain.like.LikeRepository;
import com.example.withdogandcat.domain.pet.PetRepository;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.review.ReviewRepository;
import com.example.withdogandcat.domain.shop.ShopRepository;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.domain.user.UserRepository;
import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.email.Email;
import com.example.withdogandcat.global.email.EmailRepository;
import com.example.withdogandcat.global.exception.CustomException;
import com.example.withdogandcat.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final PetRepository petRepository;
    private final ImageRepository imageRepository;
    private final ImageS3Service imageS3Service;
    private final ShopRepository shopRepository;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;

    @Transactional
    public void registerNewAccount(SignupRequestDto requestDto) {
        checkIfEmailExist(requestDto.getEmail());

        Email email = emailRepository.findByEmailAndExpiryDateAfterAndEmailVerifiedTrue(
                        requestDto.getEmail(), LocalDateTime.now())
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User newUser = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .phoneNumber(requestDto.getPhoneNumber())
                .nickname(requestDto.getNickname())
                .role(UserRole.USER)
                .build();

        userRepository.save(newUser);
        emailRepository.delete(email);
    }

    private void checkIfEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }


    @Transactional
    public void deleteUnverifiedEmails(LocalDateTime now) {
        List<Email> emails = emailRepository.findAll();
        for (Email email : emails) {
            if (email.isEmailVerified() && !email.isRegistrationComplete() && email.getExpiryDate().isBefore(now)) {
                emailRepository.delete(email);
            }
        }
    }

    @Transactional
    public void deleteAccount(Long userId, String inputPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 댓글과 좋아요 삭제
        likeRepository.deleteByUser(user);
        reviewRepository.deleteByUser(user);

        // 사용자와 관련된 모든 이미지 조회
        List<Image> userImages = imageRepository.findByUserId(userId);

        // S3와 데이터베이스에서 이미지 삭제
        imageS3Service.deleteImages(userImages);

        // 펫 삭제
        petRepository.deleteByUser(user);

        // 샵 삭제
        shopRepository.deleteByUser(user);

        // 유저 삭제
        userRepository.delete(user);
    }
}

package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.email.Email;
import com.example.withdogandcat.domain.email.EmailRepository;
import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.image.ImageRepository;
import com.example.withdogandcat.domain.image.ImageS3Service;
import com.example.withdogandcat.domain.like.LikeRepository;
import com.example.withdogandcat.domain.pet.PetRepository;
import com.example.withdogandcat.domain.review.ReviewRepository;
import com.example.withdogandcat.domain.shop.ShopRepository;
import com.example.withdogandcat.domain.user.dto.SignupRequestDto;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.domain.user.entity.UserRole;
import com.example.withdogandcat.global.common.BaseResponse;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
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
    public BaseResponse<User> registerNewAccount(SignupRequestDto requestDto) throws BaseException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BaseException(BaseResponseStatus.EMAIL_ALREADY_EXISTS);
        }

        Email email = emailRepository.findByEmailAndExpiryDateAfterAndEmailVerifiedTrue(
                        requestDto.getEmail(), LocalDateTime.now())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.EMAIL_NOT_FOUND));

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

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "회원가입 성공", newUser);
    }

    private BaseResponse<Void> checkIfEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            return new BaseResponse<>(BaseResponseStatus.EMAIL_ALREADY_EXISTS, "이미 사용중인 이메일 주소입니다.", null);
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "사용 가능한 이메일 주소입니다.", null);
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
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }

        likeRepository.deleteByUser(user);
        reviewRepository.deleteByUser(user);

        List<Image> userImages = imageRepository.findByUserId(userId);

        imageS3Service.deleteImages(userImages);

        petRepository.deleteByUser(user);

        shopRepository.deleteByUser(user);

        userRepository.delete(user);
    }
}
package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.chat.repo.ChatRoomJpaRepository;
import com.example.withdogandcat.domain.chat.repo.ChatRoomRepository;
import com.example.withdogandcat.domain.chat.service.ChatMessageService;
import com.example.withdogandcat.domain.image.Image;
import com.example.withdogandcat.domain.image.ImageRepository;
import com.example.withdogandcat.domain.image.ImageS3Service;
import com.example.withdogandcat.domain.like.LikeRepository;
import com.example.withdogandcat.domain.pet.PetRepository;
import com.example.withdogandcat.domain.review.ReviewRepository;
import com.example.withdogandcat.domain.shop.ShopRepository;
import com.example.withdogandcat.domain.user.entity.User;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDeletionService {

    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final PetRepository petRepository;
    private final ImageRepository imageRepository;
    private final ImageS3Service imageS3Service;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageService chatMessageService;


    /**
     * 회원탈퇴
     */
    @Transactional
    public void deleteAccount(Long userId, String inputPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }

        // 사용자가 생성한 채팅방에 대한 처리
        List<ChatRoomEntity> userChatRooms = chatRoomJpaRepository.findByCreatorId(user);
        for (ChatRoomEntity room : userChatRooms) {

            // 먼저 MySQL 및 Redis의 채팅 메시지 삭제
            chatMessageService.deleteMessages(room.getRoomId());

            // 그 다음 MySQL 및 Redis의 채팅방 삭제
            chatRoomRepository.deleteRoom(room.getRoomId());
            chatRoomJpaRepository.delete(room);
        }


        likeRepository.deleteByUser(user);
        reviewRepository.deleteByUser(user);

        List<Image> userImages = imageRepository.findByUserId(userId);

        imageS3Service.deleteImages(userImages);
        petRepository.deleteByUser(user);
        shopRepository.deleteByUser(user);

        // 관련데이터 삭제 -> 리프레시 토큰삭제 -> 유저삭제
        redisTemplate.delete(user.getEmail());
        userRepository.delete(user);
    }

}

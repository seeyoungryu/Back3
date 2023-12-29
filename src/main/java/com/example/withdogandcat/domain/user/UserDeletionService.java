package com.example.withdogandcat.domain.user;

import com.example.mailtest.domain.Image.Image;
import com.example.mailtest.domain.Image.ImageRepository;
import com.example.mailtest.domain.Image.ImageS3Service;
import com.example.mailtest.domain.chat.entity.ChatRoomEntity;
import com.example.mailtest.domain.chat.hashtag.ChatRoomTagMap;
import com.example.mailtest.domain.chat.hashtag.ChatRoomTagMapRepository;
import com.example.mailtest.domain.chat.hashtag.Tag;
import com.example.mailtest.domain.chat.hashtag.TagRepository;
import com.example.mailtest.domain.chat.repo.ChatRoomJpaRepository;
import com.example.mailtest.domain.chat.repo.ChatRoomRepository;
import com.example.mailtest.domain.chat.service.ChatMessageService;
import com.example.mailtest.domain.like.LikeRepository;
import com.example.mailtest.domain.pet.PetRepository;
import com.example.mailtest.domain.review.ReviewRepository;
import com.example.mailtest.domain.shop.ShopRepository;
import com.example.mailtest.domain.shop.entity.Shop;
import com.example.mailtest.domain.user.entity.User;
import com.example.mailtest.global.exception.BaseException;
import com.example.mailtest.global.exception.BaseResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDeletionService {

    /**
     * 회원탈퇴
     */

    private final TagRepository tagRepository;
    private final PetRepository petRepository;
    private final LikeRepository likeRepository;
    private final ImageS3Service imageS3Service;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ChatRoomTagMapRepository chatRoomTagMapRepository;

    @Transactional
    public void deleteAccount(Long userId, String inputPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }

        likeRepository.deleteByUser(user);

        reviewRepository.deleteByUser(user);

        List<Shop> userShops = shopRepository.findByUser(user);
        for (Shop shop : userShops) {
            reviewRepository.deleteByShop(shop);
        }
        shopRepository.deleteAll(userShops);
        shopRepository.deleteByUser(user);

        List<Image> userImages = imageRepository.findByUserId(userId);
        imageS3Service.deleteImages(userImages);

        petRepository.deleteByUser(user);

        chatMessageService.deleteAllMessagesByUser(userId);

        List<ChatRoomEntity> userChatRooms = chatRoomJpaRepository.findByCreatorId(user);

        for (ChatRoomEntity room : userChatRooms) {

            List<ChatRoomTagMap> tagMaps = chatRoomTagMapRepository.findByChatRoom(room);

            for (ChatRoomTagMap tagMap : tagMaps) {
                Tag tag = tagMap.getTag();
                chatRoomTagMapRepository.delete(tagMap);

                long count = chatRoomTagMapRepository.countByTag(tag);
                if (count == 0) {
                    tagRepository.delete(tag);
                }
            }

            chatMessageService.deleteMessages(room.getRoomId());

            chatRoomRepository.deleteRoom(room.getRoomId());
            chatRoomJpaRepository.delete(room);
        }

        redisTemplate.delete(user.getEmail());
        userRepository.delete(user);
    }

}

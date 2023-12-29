package com.example.withdogandcat.domain.user;

import com.example.withdogandcat.domain.chat.entity.ChatMessageEntity;
import com.example.withdogandcat.domain.chat.entity.ChatRoomEntity;
import com.example.withdogandcat.domain.chat.entity.MessageType;
import com.example.withdogandcat.domain.chat.hashtag.ChatRoomTagMap;
import com.example.withdogandcat.domain.chat.hashtag.ChatRoomTagMapRepository;
import com.example.withdogandcat.domain.chat.hashtag.TagRepository;
import com.example.withdogandcat.domain.chat.repo.ChatMessageJpaRepository;
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
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomTagMapRepository chatRoomTagMapRepository;

    @Transactional
    public void deleteAccount(Long userId, String inputPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MISMATCH);
        }

        List<ChatMessageEntity> userMessages = chatMessageJpaRepository.findBySender_UserId(userId);
        for (ChatMessageEntity message : userMessages) {
            message.setType(MessageType.DELETED_USER_MESSAGE);
            chatMessageJpaRepository.save(message);
        }

        List<ChatRoomEntity> userChatRooms = chatRoomJpaRepository.findByCreatorId(user);
        for (ChatRoomEntity room : userChatRooms) {

            List<ChatRoomTagMap> tagMaps = chatRoomTagMapRepository.findByChatRoom(room);
            for (ChatRoomTagMap tagMap : tagMaps) {
                chatRoomTagMapRepository.delete(tagMap);

                if (chatRoomTagMapRepository.countByTag(tagMap.getTag()) == 0) {
                    tagRepository.delete(tagMap.getTag());
                }
            }

            chatMessageService.deleteMessages(room.getRoomId());
            chatRoomRepository.deleteRoom(room.getRoomId());
            chatRoomJpaRepository.delete(room);
        }

        likeRepository.deleteByUser(user);
        reviewRepository.deleteByUser(user);
        List<Image> userImages = imageRepository.findByUserId(userId);
        imageS3Service.deleteImages(userImages);
        petRepository.deleteByUser(user);
        shopRepository.deleteByUser(user);

        redisTemplate.delete(user.getEmail());

        user.delete();
        userRepository.save(user);
    }

}


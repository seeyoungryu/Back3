package com.example.withdogandcat.domain.Image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.shop.entity.Shop;
import com.example.withdogandcat.global.exception.BaseException;
import com.example.withdogandcat.global.exception.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageS3Service {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    /**
     * 가게 이미지
     */
    public List<Image> uploadMultipleImagesForShop(List<MultipartFile> multipartFiles, Shop shop) throws IOException {
        return uploadMultipleImages(multipartFiles, shop, null);
    }

    /**
     * 펫 이미지
     */
    public List<Image> uploadMultipleImagesForPet(List<MultipartFile> multipartFiles, Pet pet) throws IOException {
        return uploadMultipleImages(multipartFiles, null, pet);
    }

    /**
     * 이미지 등록
     */
    private List<Image> uploadMultipleImages(List<MultipartFile> multipartFiles, Shop shop, Pet pet) throws IOException {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }

        return multipartFiles.stream()
                .map(multipartFile -> uploadSingleImage(multipartFile, shop, pet))
                .collect(Collectors.toList());
    }

    private Image uploadSingleImage(MultipartFile multipartFile, Shop shop, Pet pet) {
        String storedImagePath = uploadFileToS3(multipartFile);
        String originName = multipartFile.getOriginalFilename();

        if (shop != null && pet != null) {
            throw new BaseException(BaseResponseStatus.IMAGE_UPLOAD_FAILED);
        }

        Image image = Image.builder()
                .originName(originName)
                .storedImagePath(storedImagePath)
                .shop(shop)
                .pet(pet)
                .build();

        return imageRepository.save(image);
    }

    private String uploadFileToS3(MultipartFile image) {
        String originName = image.getOriginalFilename();
        String ext = originName.substring(originName.lastIndexOf("."));
        String changedName = UUID.randomUUID().toString() + ext;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, changedName, image.getInputStream(), metadata));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }
        return amazonS3.getUrl(bucketName, changedName).toString();
    }

    /**
     * 이미지 삭제
     */
    public void deleteImages(List<Image> images) {
        if (images != null) {
            images.stream()
                    .map(Image::getStoredImagePath)
                    .map(this::extractFileName)
                    .forEach(fileName -> amazonS3.deleteObject(bucketName, fileName));

            imageRepository.deleteAll(images);
        }
    }

    /**
     * 이미지 URL 관련
     */
    private String extractFileName(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}

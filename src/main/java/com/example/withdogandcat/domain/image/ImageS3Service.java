package com.example.withdogandcat.domain.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.withdogandcat.domain.pet.entity.Pet;
import com.example.withdogandcat.domain.shop.entity.Shop;
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

    public List<Image> uploadMultipleImages(List<MultipartFile> multipartFiles, Shop shop) throws IOException {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }

        return multipartFiles.stream()
                .map(multipartFile -> uploadSingleImage(multipartFile, shop))
                .collect(Collectors.toList());
    }

    private Image uploadSingleImage(MultipartFile multipartFile, Shop shop) {
        String storedImagePath = uploadFileToS3(multipartFile);
        String originName = multipartFile.getOriginalFilename();

        Image image = Image.builder()
                .originName(originName)
                .storedImagePath(storedImagePath)
                .shop(shop)
                .build();

        return imageRepository.save(image);
    }


    public List<Image> uploadMultipleImages(List<MultipartFile> multipartFiles, Pet pet) throws IOException {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return Collections.emptyList();
        }

        return multipartFiles.stream()
                .map(multipartFile -> uploadSingleImage(multipartFile, pet))
                .collect(Collectors.toList());
    }

    private Image uploadSingleImage(MultipartFile multipartFile, Pet pet) {
        String storedImagePath = uploadFileToS3(multipartFile);
        String originName = multipartFile.getOriginalFilename();

        Image image = Image.builder()
                .originName(originName)
                .storedImagePath(storedImagePath)
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

    public void deleteImages(List<Image> images) {
        if (images != null) {
            images.forEach(this::deleteImage);
        }
    }

    public void deleteImage(Image image) {
        amazonS3.deleteObject(bucketName, extractFileName(image.getStoredImagePath()));
        imageRepository.delete(image);
    }

    private String extractFileName(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}

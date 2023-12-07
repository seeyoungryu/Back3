package com.example.withdogandcat.global.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Service
public class S3Upload {

    private final AmazonS3 amazonS3;

    public S3Upload(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // s3 -> 서버
    public InputStream downloadFile(String key, String bucketName) {
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, key));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        return objectInputStream;
    }

    //서버 -> s3 업로드
    public String upload(MultipartFile multipartFile, String bucketName) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        amazonS3.putObject(bucketName, s3FileName, multipartFile.getInputStream(), objMeta);
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public String upload2(String originalFilename, InputStream inputStream, String bucketName) throws IOException {
        String s3FileName = UUID.randomUUID() + "-" + originalFilename;
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(inputStream.available());
        amazonS3.putObject(bucketName, s3FileName, inputStream, objMeta);
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void delete(String fileUrl, String bucketName) throws UnsupportedEncodingException {
        fileUrl = URLDecoder.decode(fileUrl, "UTF-8");
        String keyName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        amazonS3.deleteObject(bucketName, keyName);
    }
}

package com.example.withdogandcat.domain.Image.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileExtensionValidator implements ConstraintValidator<FileExtension, MultipartFile> {

    private List<String> allowedExtensions;

    @Override
    public void initialize(FileExtension constraintAnnotation) {
        this.allowedExtensions = Arrays.asList(constraintAnnotation.ext());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }
}

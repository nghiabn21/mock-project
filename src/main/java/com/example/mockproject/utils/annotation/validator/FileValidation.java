package com.example.mockproject.utils.annotation.validator;

import com.example.mockproject.utils.annotation.DoubleAnotation;
import com.example.mockproject.utils.annotation.FileAnnotation;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FileValidation implements ConstraintValidator<FileAnnotation, MultipartFile> {
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if ("".equals(Objects.requireNonNull(value.getOriginalFilename()).trim())) {
            return true;
        }

        return StringUtils.endsWithIgnoreCase(value.getOriginalFilename(), ".pdf") ||
                StringUtils.endsWithIgnoreCase(value.getOriginalFilename(), ".DOC") ||
                StringUtils.endsWithIgnoreCase(value.getOriginalFilename(), ".DOCX");


    }
}

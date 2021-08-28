package com.rakuten.springboot.cdr.dto.mapper;

import com.rakuten.springboot.cdr.dto.model.cdr.FileDto;
import com.rakuten.springboot.cdr.model.file.File;

/**
 * Created by Mohit Chandak
 */
public class FileMapper {
    public static FileDto toFileDto(File file) {
        return new FileDto()
                .setFileName(file.getFileName());
    }
}

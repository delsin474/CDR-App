package com.rakuten.springboot.cdr.service;

import com.rakuten.springboot.cdr.dto.model.cdr.FileDto;
import java.util.List;

/**
 * Created by Mohit Chandak
 */
public interface FileParserService {

    //Get All Files in Folder
    List<FileDto> getAllFiles();
    
    //Parse and Store in DB
    int parseFile(String fileName);

}

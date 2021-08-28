package com.rakuten.springboot.cdr.repository.file;

import com.rakuten.springboot.cdr.model.file.File;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mohit Chandak
 */
public interface FileRepository extends CrudRepository<File, Long> {
    
    File findByFileName(String fileName);

}

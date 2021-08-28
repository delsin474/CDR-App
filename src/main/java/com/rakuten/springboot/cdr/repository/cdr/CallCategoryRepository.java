package com.rakuten.springboot.cdr.repository.cdr;

import com.rakuten.springboot.cdr.model.cdr.CallCategory;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mohit Chandak
 */
public interface CallCategoryRepository extends CrudRepository<CallCategory, Long> {
    
    CallCategory findByCallCategory(String callCategory);

}

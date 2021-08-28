package com.rakuten.springboot.cdr.repository.cdr;

import com.rakuten.springboot.cdr.model.cdr.SubscriberType;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mohit Chandak
 */
public interface SubscriberTypeRepository extends CrudRepository<SubscriberType, Long> {
    
    SubscriberType findBySubscriberType(String subscriberType);

}

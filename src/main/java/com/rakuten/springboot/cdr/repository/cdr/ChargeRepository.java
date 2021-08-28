package com.rakuten.springboot.cdr.repository.cdr;

import com.rakuten.springboot.cdr.model.cdr.CallCategory;
import com.rakuten.springboot.cdr.model.cdr.Charge;
import com.rakuten.springboot.cdr.model.cdr.ServiceType;
import com.rakuten.springboot.cdr.model.cdr.SubscriberType;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mohit Chandak
 */
public interface ChargeRepository extends CrudRepository<Charge, Long> {
    
    Charge findByServiceTypeAndCallCategoryAndSubscriberType(ServiceType serviceType,CallCategory callCategory
    ,SubscriberType subscriberType);

}

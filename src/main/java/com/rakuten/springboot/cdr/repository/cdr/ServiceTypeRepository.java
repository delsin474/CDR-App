package com.rakuten.springboot.cdr.repository.cdr;

import com.rakuten.springboot.cdr.model.cdr.ServiceType;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Mohit Chandak
 */
public interface ServiceTypeRepository extends CrudRepository<ServiceType, Long> {
    
    ServiceType findByServiceType(String serviceType);

}

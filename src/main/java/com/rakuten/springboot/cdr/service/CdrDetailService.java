package com.rakuten.springboot.cdr.service;


import com.rakuten.springboot.cdr.dto.model.cdr.AnumChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.CallCategoryDurationDto;
import com.rakuten.springboot.cdr.dto.model.cdr.ServiceChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.SubscriberDto;

import java.sql.Date;
import java.util.List;

/*
 * Created by Mohit Chandak
 */
public interface CdrDetailService {

    //Get Volume Per Day for Specific Date
    Long getVolumePerDayMinutes(Date date);

    Long getVolumePerDayMB(Date date);
    
    String getAnumHighestDuration();

    List<AnumChargeDto> getAnumByMaxChargePerDay();

    List<ServiceChargeDto> getServiceByMaxChargePerDay();
    
    List<CallCategoryDurationDto> getCallCategoryDuration();

    List<SubscriberDto> getTotalVolumeBySubscriberTypeGprs();

    Boolean generateOutputJson();
}



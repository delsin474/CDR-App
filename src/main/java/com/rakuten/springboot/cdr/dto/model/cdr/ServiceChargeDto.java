package com.rakuten.springboot.cdr.dto.model.cdr;

import java.sql.Date;

public interface ServiceChargeDto {
    Date getStartdate();
    String getServicetype();
    double getMaxcharge();
}

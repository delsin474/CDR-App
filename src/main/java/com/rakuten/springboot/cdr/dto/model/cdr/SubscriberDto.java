package com.rakuten.springboot.cdr.dto.model.cdr;

import java.sql.Date;

public interface SubscriberDto {
    Long getActualduration();
    Long getRoundedduration();
    String getFilename();
    String getSubscribertype();
    Date getDate();
}

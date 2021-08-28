package com.rakuten.springboot.cdr.dto.model.cdr;

import java.sql.Date;

public interface AnumChargeDto {
  String getAnum();
  Date getStartdate();
  double getMaxcharge();  
}

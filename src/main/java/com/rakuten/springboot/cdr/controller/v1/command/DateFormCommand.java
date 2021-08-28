package com.rakuten.springboot.cdr.controller.v1.command;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

import javax.validation.constraints.NotBlank;

/**
 * Created by Mohit Chandak
 */
@Data
@Accessors(chain = true)
public class DateFormCommand {
    @NotBlank
    private Date date;
    
}

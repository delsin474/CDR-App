package com.rakuten.springboot.cdr.controller.v1.request;

import java.sql.Date;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Mohit Chandak
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetVolumeRequest {
    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private Date date;
}

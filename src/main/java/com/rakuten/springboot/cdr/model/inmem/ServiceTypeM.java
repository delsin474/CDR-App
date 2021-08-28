package com.rakuten.springboot.cdr.model.inmem;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Mohit Chandak
 */
@Getter
@Setter
@Accessors(chain = true)
public class ServiceTypeM {
    private int id;
    private String serviceType;
    private static final AtomicInteger count = new AtomicInteger(0);
    public ServiceTypeM(String serviceType)
    {
        this.serviceType = serviceType;
        id = count.incrementAndGet();
    }
}

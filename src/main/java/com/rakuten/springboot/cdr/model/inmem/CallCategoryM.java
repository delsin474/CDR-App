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
public class CallCategoryM {
    private int id;
    private String callCategory;
    private static final AtomicInteger count = new AtomicInteger(0);
    public CallCategoryM(String callCategory)
    {
        this.callCategory = callCategory;
        id = count.incrementAndGet();
    }
}

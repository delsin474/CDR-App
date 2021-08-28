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
public class SubscriberTypeM {
    private int id;
    private String subscriberType;
    private static final AtomicInteger count = new AtomicInteger(0);
    public SubscriberTypeM(String subscriberType)
    {
        this.subscriberType = subscriberType;
        id = count.incrementAndGet();
    }
}

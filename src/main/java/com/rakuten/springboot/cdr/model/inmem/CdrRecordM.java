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
public class CdrRecordM {
    private int id;
    private String anum;
    private String bnum;
    private FileM file;
    private CallCategoryM callCategory;
    private ServiceTypeM serviceType;
    private SubscriberTypeM subscriberType;
    private Long usedAmount;
    private Long roundedAmount;
    private double charge;
    private java.sql.Timestamp startDateTime;
    private static final AtomicInteger count = new AtomicInteger(0);
    public CdrRecordM()
    {
        id = count.incrementAndGet();
    }
}

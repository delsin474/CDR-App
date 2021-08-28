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
public class FileM {
    private Integer id;
    private String fileName;
    private String fileExtension;
    private static final AtomicInteger count = new AtomicInteger(0);
    public FileM()
    {
        id = count.incrementAndGet();
    }
}

package com.rakuten.springboot.cdr.service.abstractfactory;

import java.util.List;

public interface Parser<T> {
    List<T> parseAndReturnRecords(String fileName);
}

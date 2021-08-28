package com.rakuten.springboot.cdr.service.abstractfactory;

import com.rakuten.springboot.cdr.repository.cdr.CallCategoryRepository;
import com.rakuten.springboot.cdr.repository.cdr.ChargeRepository;
import com.rakuten.springboot.cdr.repository.cdr.ServiceTypeRepository;
import com.rakuten.springboot.cdr.repository.cdr.SubscriberTypeRepository;
import com.rakuten.springboot.cdr.repository.file.FileRepository;
import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;

public interface ParserFactory<T> {
    XMLParser<T> createXMLParser(ServiceTypeRepository serviceTypeRepository, SubscriberTypeRepository subscriberTypeRepository,
    CallCategoryRepository callCategoryRepository, FileRepository fileRepository, ChargeRepository chargeRepository,CdrInmemStore cdrInmemStore);
    CSVParser<T> createCSVParser();
}

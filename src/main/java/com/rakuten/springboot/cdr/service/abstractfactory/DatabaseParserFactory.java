package com.rakuten.springboot.cdr.service.abstractfactory;

import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;
import com.rakuten.springboot.cdr.repository.cdr.CallCategoryRepository;
import com.rakuten.springboot.cdr.repository.cdr.ChargeRepository;
import com.rakuten.springboot.cdr.repository.cdr.ServiceTypeRepository;
import com.rakuten.springboot.cdr.repository.cdr.SubscriberTypeRepository;
import com.rakuten.springboot.cdr.repository.file.FileRepository;

public class DatabaseParserFactory<T> implements ParserFactory<T> {

    @Override
    public XMLParser<T> createXMLParser(ServiceTypeRepository serviceTypeRepository, SubscriberTypeRepository subscriberTypeRepository,
    CallCategoryRepository callCategoryRepository, FileRepository fileRepository, ChargeRepository chargeRepository, CdrInmemStore cdrInmemStore) {
        return new XMLDatabaseParser<T>(serviceTypeRepository, subscriberTypeRepository, callCategoryRepository, fileRepository, chargeRepository, cdrInmemStore);
    }

    @Override
    public CSVParser<T> createCSVParser() {
        return null;
    }
    
}

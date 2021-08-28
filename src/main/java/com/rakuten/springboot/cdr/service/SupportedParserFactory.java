package com.rakuten.springboot.cdr.service;

import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;
import com.rakuten.springboot.cdr.repository.cdr.*;
import com.rakuten.springboot.cdr.repository.file.*;

class  SupportedParserFactory {
    public static Parser getParser(String fileExtension,ServiceTypeRepository serviceTypeRepository, SubscriberTypeRepository subscriberTypeRepository,
    CallCategoryRepository callCategoryRepository, FileRepository fileRepository, ChargeRepository chargeRepository, CdrInmemStore cdrInmemStore) {
        if(fileExtension.compareTo("xml")==0)
            return new XMLParser(serviceTypeRepository, subscriberTypeRepository, callCategoryRepository, fileRepository, chargeRepository, cdrInmemStore);
        else if(fileExtension.compareTo("csv")==0) {
            //return CSV Parser
            return null;
        } 
        else return null; 
    }
}
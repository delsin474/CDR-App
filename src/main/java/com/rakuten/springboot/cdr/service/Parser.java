package com.rakuten.springboot.cdr.service;
import java.util.List;
import com.rakuten.springboot.cdr.model.cdr.CdrRecord;
import com.rakuten.springboot.cdr.model.inmem.CdrRecordM;

public interface Parser {
    List<CdrRecord> parseAndReturnRecords(String fileName);
    List<CdrRecordM> parseAndReturnRecordsm(String fileName);
}

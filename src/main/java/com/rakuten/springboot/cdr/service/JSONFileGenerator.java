package com.rakuten.springboot.cdr.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.rakuten.springboot.cdr.model.cdr.CdrRecord;
import com.rakuten.springboot.cdr.repository.cdr.CdrRecordRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class JSONFileGenerator implements FileGenerator {
    CdrRecordRepository cdrRecordRepository;

    @Autowired
    public JSONFileGenerator(CdrRecordRepository cdrRecordRepository) {
        this.cdrRecordRepository = cdrRecordRepository;
    }
    
    

    @Override
    public Boolean generateAndStore() {
        try {
        JSONArray parentArray = new JSONArray();
        List<CdrRecord> cdrs = new ArrayList<>();
        
        cdrRecordRepository.findByOrderByChargeAsc().forEach(cdrs::add);

        for (int i = 0; i < cdrs.size(); i++) {
            JSONObject object = new JSONObject();
            object.put("ANUM", cdrs.get(i).getAnum());
            object.put("BNUM", cdrs.get(i).getBnum());
            object.put("ServiceType", cdrs.get(i).getServiceType().getId());
            object.put("CallCategory", cdrs.get(i).getCallCategory().getId());
            object.put("SubscriberType", cdrs.get(i).getSubscriberType().getId());
            object.put("StartDatetime", cdrs.get(i).getStartDateTime());
            object.put("UsedAmount", cdrs.get(i).getUsedAmount());
            object.put("ServiceTypeName", cdrs.get(i).getServiceType().getServiceType());
            object.put("CallCategoryName", cdrs.get(i).getCallCategory().getCallCategory());
            object.put("SubscriberTypeName", cdrs.get(i).getSubscriberType().getSubscriberType());
            object.put("RoundedUsedAmount", cdrs.get(i).getRoundedAmount());
            object.put("Charge", cdrs.get(i).getCharge());
            parentArray.put(object);
        }
        
        //Define the file name of the file
        Path fileName = Path.of("CDROutput/CDROUT.json");
 
        //Write into the file
        Files.writeString(fileName, parentArray.toString());

        }
        catch(Exception e)
        {
            //Handle Exception Here
            return false;
        }
        return true;
    }

}

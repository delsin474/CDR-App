package com.rakuten.springboot.cdr.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.rakuten.springboot.cdr.dto.model.cdr.AnumChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.CallCategoryDurationDto;
import com.rakuten.springboot.cdr.dto.model.cdr.ServiceChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.SubscriberDto;
import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;
import com.rakuten.springboot.cdr.model.inmem.CdrRecordM;
import com.rakuten.springboot.cdr.repository.cdr.CdrRecordRepository;
import com.rakuten.springboot.cdr.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Mohit Chandak
 */

@Component
public class CdrDetailServiceImpl implements CdrDetailService {
    @Autowired
    private CdrRecordRepository cdrRecordRepository;

    @Autowired
    private CdrInmemStore cdrInmemStore;

    @Override
    public Long getVolumePerDayMinutes(Date date) {
        // InMem
        Long finalRoundedAmount = 0L;
        List<CdrRecordM> cdrRecordsM = cdrInmemStore.getCdrRecords();
        for (int i = 0; i < cdrRecordsM.size(); i++) {
            DateUtils dateutil = new DateUtils();
            // For Specific Date and Service Type Voice
            if (dateutil.formattedDate(date).equals(dateutil.formattedDate(cdrRecordsM.get(i).getStartDateTime()))
                    && cdrRecordsM.get(i).getServiceType().getId() == 1) {
                finalRoundedAmount += cdrRecordsM.get(i).getRoundedAmount();
            }
        }
        return finalRoundedAmount;
    }

    @Override
    public Long getVolumePerDayMB(Date date) {
        // DB
        return cdrRecordRepository.findVolumePerDay(date);
    }

    @Override
    public String getAnumHighestDuration() {
        // DB
        // return cdrRecordRepository.findAnumLongestDurationVoice();
        String anumHighest = null;
        Long duration = 0L;
        List<CdrRecordM> cdrRecordsM = cdrInmemStore.getCdrRecords();
        for (int i = 0; i < cdrRecordsM.size(); i++) {
            if (cdrRecordsM.get(i).getRoundedAmount() > duration && cdrRecordsM.get(i).getServiceType().getId() == 1) {
                anumHighest = cdrRecordsM.get(i).getAnum();
            }
        }
        return anumHighest;
    }

    @Override
    public List<AnumChargeDto> getAnumByMaxChargePerDay() {
        return cdrRecordRepository.findAnumsWithMaxChargesPerDay();
    }

    @Override
    public List<CallCategoryDurationDto> getCallCategoryDuration() {
        List<CallCategoryDurationDto> ansList = new ArrayList<>();
        HashMap<Integer, Long> catDuration = new HashMap<>();
        List<CdrRecordM> cdrRecordsM = cdrInmemStore.getCdrRecords();
        for (int i = 0; i < cdrRecordsM.size(); i++) {
            Integer currentKey = cdrRecordsM.get(i).getCallCategory().getId();

            if (cdrRecordsM.get(i).getServiceType().getId() == 1 && catDuration.containsKey(currentKey)) {
                catDuration.put(currentKey, catDuration.get(currentKey) + cdrRecordsM.get(i).getRoundedAmount());
            }
            else if(cdrRecordsM.get(i).getServiceType().getId() == 1) {
                catDuration.put(currentKey, cdrRecordsM.get(i).getRoundedAmount());    
            }
        }

        Iterator<HashMap.Entry<Integer, Long>> itr = catDuration.entrySet().iterator();
         
        while(itr.hasNext())
        {
             HashMap.Entry<Integer, Long> entry = itr.next();
             CallCategoryDurationDto callCategoryDurationDto = new CallCategoryDurationDto()
             .setCallCategory(cdrInmemStore.getCallCategoryType(entry.getKey()).getCallCategory())
             .setRoundedDuration(entry.getValue());
             ansList.add(callCategoryDurationDto);
        }

        
        return ansList;
    }

    @Override
    public List<ServiceChargeDto> getServiceByMaxChargePerDay() {
        return cdrRecordRepository.findServiceWithMaxChargesPerDay();
    }

    @Override
    public Boolean generateOutputJson() {
        JSONFileGenerator jsonFileGenerator = new JSONFileGenerator(cdrRecordRepository);
        return jsonFileGenerator.generateAndStore();
    }

    @Override
    public List<SubscriberDto> getTotalVolumeBySubscriberTypeGprs() {
        return cdrRecordRepository.findGprsVolumeSubscriber();
    }

}

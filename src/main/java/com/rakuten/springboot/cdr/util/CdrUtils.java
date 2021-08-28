package com.rakuten.springboot.cdr.util;

import com.rakuten.springboot.cdr.model.cdr.CallCategory;
import com.rakuten.springboot.cdr.model.cdr.Charge;
import com.rakuten.springboot.cdr.model.cdr.ServiceType;
import com.rakuten.springboot.cdr.model.cdr.SubscriberType;
import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;
import com.rakuten.springboot.cdr.repository.cdr.ChargeRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class CdrUtils {
    ChargeRepository chargeRepository;
    CdrInmemStore cdrInmemStore;

    @Autowired
    public CdrUtils(ChargeRepository chargeRepository, CdrInmemStore cdrInmemStore) {
        this.chargeRepository = chargeRepository;
        this.cdrInmemStore = cdrInmemStore;
    }

    // Get duration in Minutes and volume in MB from UsedAmount
    public long getRoundedUsedAmount(Long usedAmount, Long serviceType) {
        Long roundedAmount = 0L;
        // Voice - 1
        if (serviceType == 1) {
            roundedAmount = usedAmount / 60;
            if ((usedAmount % 60) != 0)
                roundedAmount++;
        }
        // SMS - 2
        else if (serviceType == 2)
            roundedAmount = 1L;
        // GPRS - 3
        else if (serviceType == 3) {
            roundedAmount = usedAmount / 1024;
            if ((usedAmount % 1024) != 0)
                roundedAmount++;
        }
        return roundedAmount;
    }

    // Normalize BNUM to have format without 00 / +
    public String normalizeBnum(String bnum) {
        if (bnum.length() == 12 && bnum.startsWith("00"))
            return bnum.substring(2);
        else if (bnum.startsWith("+"))
            return bnum.substring(1);
        return bnum;
    }

    // Calculate Charge
    public double calculateCharge(Long roundedUsedAmount, ServiceType serviceType, CallCategory callCategory,
            SubscriberType subscriberType) {
        Charge charge = chargeRepository.findByServiceTypeAndCallCategoryAndSubscriberType(serviceType, callCategory,
                subscriberType);
        return roundedUsedAmount * charge.getCharge_usd();
    }
    
    //Calculate Charge InMem
    public double calculateChargeInMem(Long roundedUsedAmount, Long serviceType, Long callCategory,
            Long subscriberType) {
        Double charge = cdrInmemStore.getCharge(serviceType.toString() + callCategory.toString() + subscriberType.toString());         
        return roundedUsedAmount * charge;
    }

}

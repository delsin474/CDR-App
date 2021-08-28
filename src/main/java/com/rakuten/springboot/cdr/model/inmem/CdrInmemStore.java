package com.rakuten.springboot.cdr.model.inmem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by Mohit Chandak
 */
@Getter
@Setter
@Accessors(chain = true)
public class CdrInmemStore {
    private List<CdrRecordM> cdrRecords;

    private HashMap<Integer, ServiceTypeM> serviceTypes;
    private HashMap<Integer, CallCategoryM> callCategories;
    private HashMap<Integer, SubscriberTypeM> subscriberTypes;

    // Here String is Ordered Concatenation of Ids for Service Type, Call Category,
    // Subscriber Type
    // Example : <"111",0.3>
    private HashMap<String, Double> charges;

    public CdrInmemStore() {
        this.cdrRecords = new ArrayList<CdrRecordM>();
        this.serviceTypes = new HashMap<Integer, ServiceTypeM>();
        this.callCategories = new HashMap<Integer, CallCategoryM>();
        this.subscriberTypes = new HashMap<Integer, SubscriberTypeM>();
        this.charges = new HashMap<String, Double>();
    }

    public void addCdrRecords(List<CdrRecordM> newCdrRecords) {
        System.out.println("New Records : " + newCdrRecords.size());
        for (int i = 0; i < newCdrRecords.size(); i++)
            cdrRecords.add(newCdrRecords.get(i));
    }

    public void addServiceType(ServiceTypeM serviceType) {
        serviceTypes.put(serviceType.getId(), serviceType);
    }

    public void addCallCategory(CallCategoryM callCategory) {
        callCategories.put(callCategory.getId(), callCategory);
    }

    public void addSubscriberType(SubscriberTypeM subscriberType) {
        subscriberTypes.put(subscriberType.getId(), subscriberType);
    }

    public void addCharge(String conIdString, Double charge) {
        charges.put(conIdString, charge);
    }

    public ServiceTypeM getServiceType(int id) {
        return serviceTypes.get(id);
    }

    public CallCategoryM getCallCategoryType(int id) {
        return callCategories.get(id);
    }

    public SubscriberTypeM getSubscriberType(int id) {
        return subscriberTypes.get(id);
    }

    public Double getCharge(String hashString) {
        System.out.println("HashString" + hashString);
        return charges.get(hashString);
    }

}

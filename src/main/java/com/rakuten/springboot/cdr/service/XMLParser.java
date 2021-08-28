package com.rakuten.springboot.cdr.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.rakuten.springboot.cdr.model.cdr.CallCategory;
import com.rakuten.springboot.cdr.model.cdr.CdrRecord;
import com.rakuten.springboot.cdr.model.cdr.ServiceType;
import com.rakuten.springboot.cdr.model.cdr.SubscriberType;
import com.rakuten.springboot.cdr.model.inmem.CallCategoryM;
import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;
import com.rakuten.springboot.cdr.model.inmem.CdrRecordM;
import com.rakuten.springboot.cdr.model.inmem.ServiceTypeM;
import com.rakuten.springboot.cdr.model.inmem.SubscriberTypeM;
import com.rakuten.springboot.cdr.repository.cdr.CallCategoryRepository;
import com.rakuten.springboot.cdr.repository.cdr.ChargeRepository;
import com.rakuten.springboot.cdr.repository.cdr.ServiceTypeRepository;
import com.rakuten.springboot.cdr.repository.cdr.SubscriberTypeRepository;
import com.rakuten.springboot.cdr.repository.file.FileRepository;
import com.rakuten.springboot.cdr.util.*;

public class XMLParser implements Parser {
    ServiceTypeRepository serviceTypeRepository;
    SubscriberTypeRepository subscriberTypeRepository;
    CallCategoryRepository callCategoryRepository;
    FileRepository fileRepository;
    ChargeRepository chargeRepository;
    CdrInmemStore cdrInmemStore;

    @Autowired
    public XMLParser(ServiceTypeRepository serviceTypeRepository, SubscriberTypeRepository subscriberTypeRepository,
            CallCategoryRepository callCategoryRepository, FileRepository fileRepository, ChargeRepository chargeRepository, CdrInmemStore cdrInmemStore) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.callCategoryRepository = callCategoryRepository;
        this.subscriberTypeRepository = subscriberTypeRepository;
        this.fileRepository = fileRepository;
        this.chargeRepository = chargeRepository;
        this.cdrInmemStore = cdrInmemStore;
    }

    @Override
    public List<CdrRecord> parseAndReturnRecords(String fileName) {
        CdrUtils cdrUtils = new CdrUtils(chargeRepository,cdrInmemStore);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<CdrRecord> cdrList = new ArrayList<>();
        try {

            // Process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // Parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File("CDRFiles/" + fileName));

            doc.getDocumentElement().normalize();

            // Get <CDR>
            NodeList list = doc.getElementsByTagName("CDR");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    // Debug
                    // String bnum = element.getElementsByTagName("BNUM").item(0).getTextContent();
                    // String anum = element.getElementsByTagName("ANUM").item(0).getTextContent();
                    // String serviceType =
                    // element.getElementsByTagName("ServiceType").item(0).getTextContent();
                    // String callCategory =
                    // element.getElementsByTagName("CallCategory").item(0).getTextContent();
                    // String subscriberType =
                    // element.getElementsByTagName("SubscriberType").item(0).getTextContent();
                    // String startDatetime =
                    // element.getElementsByTagName("StartDatetime").item(0).getTextContent();
                    // String usedAmount =
                    // element.getElementsByTagName("UsedAmount").item(0).getTextContent();

                    Optional<ServiceType> sType = serviceTypeRepository.findById(Long
                            .valueOf(element.getElementsByTagName("ServiceType").item(0).getTextContent()).longValue());
                    Optional<SubscriberType> subType = subscriberTypeRepository.findById(
                            Long.parseLong(element.getElementsByTagName("SubscriberType").item(0).getTextContent()));
                    Optional<CallCategory> callCategory = callCategoryRepository.findById(
                            Long.parseLong(element.getElementsByTagName("CallCategory").item(0).getTextContent()));

                    DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = df.parse(element.getElementsByTagName("StartDatetime").item(0).getTextContent());
                    long time = date.getTime();
                    Timestamp ts = new Timestamp(time);

                    //Used Amount
                    Long usedAmountL;
                    String usedAmount = element.getElementsByTagName("UsedAmount").item(0).getTextContent();
                    if(usedAmount.compareTo("")==0)
                        usedAmountL = 0L;
                    else usedAmountL = Long.parseLong(usedAmount);   

                    //Rounded Amount
                    Long roundedAmountL = cdrUtils.getRoundedUsedAmount(usedAmountL,sType.get().getId());
                    
                    // Debug
                    // SubscriberType subscriberTypeFinal = subType.get();
                    // System.out.println(subscriberTypeFinal.getSubscriberType());
                    // ServiceType serviceTypeFinal = sType.get();
                    // System.out.println(serviceTypeFinal.getServiceType());
                    // CallCategory callCategory2 = callCategory.get();
                    // System.out.println(callCategory2.getCallCategory());

                    //Charge
                    double finalCharge = cdrUtils.calculateCharge(roundedAmountL, sType.get(), callCategory.get(), subType.get());

                    CdrRecord cdrRecord = new CdrRecord()
                            .setAnum(element.getElementsByTagName("ANUM").item(0).getTextContent())
                            .setBnum(cdrUtils.normalizeBnum(element.getElementsByTagName("BNUM").item(0).getTextContent()))
                            .setServiceType(sType.get()).setCallCategory(
                                    callCategory.get())
                            .setSubscriberType(subType.get())
                            .setUsedAmount(usedAmountL)
                            .setRoundedAmount(roundedAmountL)
                            .setFile(fileRepository.findByFileName(fileName.substring(0, fileName.lastIndexOf("."))))
                            .setStartDateTime(ts)
                            .setCharge(finalCharge);

                    cdrList.add(cdrRecord);

                }
            }

        } catch (ParserConfigurationException | SAXException | java.text.ParseException | IOException e) {
            e.printStackTrace();
        }
        return cdrList;
    }

    @Override
    public List<CdrRecordM> parseAndReturnRecordsm(String fileName) {
        CdrUtils cdrUtils = new CdrUtils(chargeRepository,cdrInmemStore);
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<CdrRecordM> cdrList = new ArrayList<>();
        try {

            // Process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // Parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File("CDRFiles/" + fileName));

            doc.getDocumentElement().normalize();

            // Get <CDR>
            NodeList list = doc.getElementsByTagName("CDR");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    ServiceTypeM sType = cdrInmemStore.getServiceType(Integer.parseInt(element.getElementsByTagName("ServiceType").item(0).getTextContent()));
                    SubscriberTypeM subType = cdrInmemStore.getSubscriberType(Integer.parseInt(element.getElementsByTagName("SubscriberType").item(0).getTextContent()));
                    CallCategoryM callCategory = cdrInmemStore.getCallCategoryType(Integer.parseInt(element.getElementsByTagName("CallCategory").item(0).getTextContent()));

                    DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = df.parse(element.getElementsByTagName("StartDatetime").item(0).getTextContent());
                    long time = date.getTime();
                    Timestamp ts = new Timestamp(time);

                    //Used Amount
                    Long usedAmountL;
                    String usedAmount = element.getElementsByTagName("UsedAmount").item(0).getTextContent();
                    if(usedAmount.compareTo("")==0)
                        usedAmountL = 0L;
                    else usedAmountL = Long.parseLong(usedAmount);   

                    //Rounded Amount
                    Long roundedAmountL = cdrUtils.getRoundedUsedAmount(usedAmountL,Long.valueOf(sType.getId()));

                    //Charge
                    double finalCharge = cdrUtils.calculateChargeInMem(roundedAmountL, Long.valueOf(sType.getId()), Long.valueOf(callCategory.getId()), Long.valueOf(subType.getId()));

                    CdrRecordM cdrRecord = new CdrRecordM()
                            .setAnum(element.getElementsByTagName("ANUM").item(0).getTextContent())
                            .setBnum(cdrUtils.normalizeBnum(element.getElementsByTagName("BNUM").item(0).getTextContent()))
                            .setServiceType(sType)
                            .setCallCategory(callCategory)
                            .setSubscriberType(subType)
                            .setUsedAmount(usedAmountL)
                            .setRoundedAmount(roundedAmountL)
                            .setStartDateTime(ts)
                            .setCharge(finalCharge);
                    
                            
                    cdrList.add(cdrRecord);

                }
            }

        } catch (ParserConfigurationException | SAXException | java.text.ParseException | IOException e) {
            e.printStackTrace();
        }
        return cdrList;
    }

    
}

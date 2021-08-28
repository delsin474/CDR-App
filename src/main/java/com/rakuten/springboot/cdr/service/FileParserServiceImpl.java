package com.rakuten.springboot.cdr.service;

import java.io.File;
import com.rakuten.springboot.cdr.dto.model.cdr.FileDto;

import com.rakuten.springboot.cdr.model.cdr.CdrRecord;
import com.rakuten.springboot.cdr.model.inmem.CdrInmemStore;
import com.rakuten.springboot.cdr.model.inmem.CdrRecordM;
import com.rakuten.springboot.cdr.repository.cdr.CallCategoryRepository;
import com.rakuten.springboot.cdr.repository.cdr.CdrRecordRepository;
import com.rakuten.springboot.cdr.repository.cdr.ChargeRepository;
import com.rakuten.springboot.cdr.repository.cdr.ServiceTypeRepository;
import com.rakuten.springboot.cdr.repository.cdr.SubscriberTypeRepository;
import com.rakuten.springboot.cdr.repository.file.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Mohit Chandak
 */
@Component
public class FileParserServiceImpl implements FileParserService {

    @Autowired
    private CdrRecordRepository cdrRecordRepository;

    @Autowired
    private CallCategoryRepository callCategoryRepository;

    @Autowired
    private SubscriberTypeRepository subscriberTypeRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private CdrInmemStore cdrInmemStore;

    /**
     * Retruns all the available files in directory.
     *
     * @return
     */
    @Override
    public List<FileDto> getAllFiles() {
        List<FileDto> fileList = new ArrayList<>();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        File directoryPath = new File("CDRFiles/");
        String contents[] = directoryPath.list();
        System.out.println(contents);
        for (int i = 0; i < contents.length; i++) {
            System.out.println(contents[i]);
            String fileNameInitial = contents[i].substring(0, contents[i].lastIndexOf("."));
            String fileNameExtension = contents[i].substring(contents[i].lastIndexOf(".") + 1);
            FileDto fileDto = new FileDto().setFileName(fileNameInitial).setFileExtension(fileNameExtension);
            fileList.add(fileDto);
        }
        return fileList;
    }

    /**
     * Parse the file and Store all the CDR Records in Database
     * 
     * @param fileName
     * @return
     */

    @Override
    public int parseFile(String fileName) {
        List<FileDto> fileList = getAllFiles();
        for (int i = 0; i < fileList.size(); i++) {
            String fileNameInitial = fileName.substring(0, fileName.lastIndexOf("."));
            String fileNameExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

            if (fileList.get(i).getFileName().compareTo(fileNameInitial) == 0) {

                // If File is not present
                com.rakuten.springboot.cdr.model.file.File file = fileRepository.findByFileName(fileNameInitial);
                if (file == null) {
                    // Insert File info in DB
                    file = new com.rakuten.springboot.cdr.model.file.File().setFileName(fileNameInitial)
                            .setFileExtension(fileNameExtension);
                    fileRepository.save(file);
                    // Parse file and Create CDR Records
                    // Call specific parser on the basis of File Extension
                    Parser parser = SupportedParserFactory.getParser(fileNameExtension, serviceTypeRepository, subscriberTypeRepository,
                    callCategoryRepository, fileRepository, chargeRepository,cdrInmemStore);    
                    
                    //XMLParser xmlParser = new XMLParser(serviceTypeRepository, subscriberTypeRepository,
                    //        callCategoryRepository, fileRepository, chargeRepository);
                    
                    List<CdrRecord> cdrList = parser.parseAndReturnRecords(fileName);
                    List<CdrRecordM> cdrListM = parser.parseAndReturnRecordsm(fileName);
                    for (int j = 0; j < cdrList.size(); j++) {
                        cdrRecordRepository.save(cdrList.get(j));
                    }
                    cdrInmemStore.addCdrRecords(cdrListM);
                    return cdrList.size();
                    
                }
                // Throw Exception Here - File Data Already Present
                return -1;
            }
        }
        return -2;
    }

}

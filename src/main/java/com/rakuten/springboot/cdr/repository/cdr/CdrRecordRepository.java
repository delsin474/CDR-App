package com.rakuten.springboot.cdr.repository.cdr;

import java.sql.Date;
import java.util.List;

import com.rakuten.springboot.cdr.dto.model.cdr.AnumChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.ServiceChargeDto;
import com.rakuten.springboot.cdr.dto.model.cdr.SubscriberDto;
import com.rakuten.springboot.cdr.model.cdr.CdrRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


/**
 * Created by Mohit Chandak
 */
public interface CdrRecordRepository extends CrudRepository<CdrRecord, Long> {
    @Query("SELECT SUM(cdr.roundedAmount) FROM CdrRecord cdr WHERE cdr.serviceType.id = 3 AND DATE(cdr.startDateTime) = ?1")    
    Long findVolumePerDay(Date date);

    @Query(nativeQuery = true, value ="SELECT anum FROM cdr_records WHERE service_type_id = 1 ORDER BY rounded_amount DESC LIMIT 1")    
    String findAnumLongestDurationVoice();

    @Query(nativeQuery = true, value ="SELECT anum,start_date as startdate,max(charge) as maxcharge FROM (SELECT DATE(start_date_time) as start_date,anum,charge FROM cdr_records) as datecharge GROUP BY datecharge.start_date")    
    List<AnumChargeDto> findAnumsWithMaxChargesPerDay();

    @Query(nativeQuery = true, value ="SELECT start_date as startdate ,service_type_name as servicetype ,max(charge) as maxcharge FROM (SELECT DATE(start_date_time) as start_date,service_type_id,charge FROM cdr_records) as datecharge,service_type  where datecharge.service_type_id = datecharge.service_type_id GROUP BY datecharge.start_date")    
    List<ServiceChargeDto> findServiceWithMaxChargesPerDay();

    @Query(nativeQuery = true, value="SELECT SUM(USED_AMOUNT) as actualduration ,SUM(ROUNDED_AMOUNT) as roundedduration ,(SELECT file_name from file f where f.file_id = cdr.file_id) as filename,(SELECT subscriber_type_name from subscriber_type s where s.subscriber_type_id = cdr.subscriber_type_id) as subscribertype,DATE(start_date_time) as date FROM CDR_RECORDS cdr WHERE service_type_id = 3 GROUP BY  DATE(start_date_time),file_id,subscriber_type_id;")
    List<SubscriberDto> findGprsVolumeSubscriber();

    List<CdrRecord> findByOrderByChargeAsc();

    

}

package com.rakuten.springboot.cdr.model.cdr;

import com.rakuten.springboot.cdr.model.file.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Created by Mohit Chandak
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "cdr_records")
public class CdrRecord {
    @Id
    @Column(name = "cdr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String anum;

    private String bnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_category_id")
    private CallCategory callCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_type_id")
    private SubscriberType subscriberType;

    private Long usedAmount;

    private Long roundedAmount;

    private double charge;

    @Column(name = "start_date_time")
    private java.sql.Timestamp startDateTime;

}

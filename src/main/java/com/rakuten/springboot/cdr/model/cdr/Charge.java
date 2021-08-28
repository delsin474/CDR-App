package com.rakuten.springboot.cdr.model.cdr;

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
@Table(name = "charges")
public class Charge {
    @Id
    @Column(name = "charge_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "call_category_id")
    private CallCategory callCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_type_id")
    private SubscriberType subscriberType;
    
    private double charge_usd;

}

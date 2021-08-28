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
@Table(name = "subscriber_type")
public class SubscriberType {
    @Id
    @Column(name = "subscriber_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscriber_type_name")
    private String subscriberType;
}

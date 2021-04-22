package com.optum.web.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="member_redis")
public class Member implements Serializable {

    private static final long serialVersionUID = -6178054163022548905L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="MBR_NM", length = 256, nullable = false)
    private String memberName;
    
    @Column(name="MBR_GDR_CD")
    private String gender;
    
    @Column(name="ADR_LN_1_TXT", length = 256, nullable = false)
    private String address1;
    
    
    @Column(name="ADR_LN_2_TXT", length = 256, nullable = false)
    private String address2;
    
    @Column(name="CITY_NM")
    private String city;
    
    @Column(name="ZIP_CD")
    private String zipCode;
    
    @Column(name="MBR_PHONE")
    private Long phone;
    
    @Column(name="CREATED_AT")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    
    @Column(name="EMAIL")
    private String email;
    

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
    
  
    
}
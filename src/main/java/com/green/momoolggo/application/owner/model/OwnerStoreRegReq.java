package com.green.momoolggo.application.owner.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

@Getter
@Setter
public class OwnerStoreRegReq {

    private long userId;

    private String storeName;
    private String location;
    private String storeTel;
    private String businessName;
    private String businessNumber;
    private String storePic;
    private String storeInfo; //가게 소개글

}

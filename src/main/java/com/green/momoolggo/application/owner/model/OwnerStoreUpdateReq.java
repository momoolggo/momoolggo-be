package com.green.momoolggo.application.owner.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerStoreUpdateReq {
    private String storeId;
    private String storeName;
    private String location;
    private String tel;
    private String businessNumber;
    private String storePic;
    private String storeInfo; //가게 소개글

}

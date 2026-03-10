package com.green.momoolggo.application.owner.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerMenuRegReq { //메뉴 등록
    private long storeId;
    private String name;
    private String menuInfo;
    private int price;
    private String menuPic;
}

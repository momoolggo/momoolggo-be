package com.green.momoolggo.application.owner.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OwnerOrderRes { // 주문 리스트 조회용
    private Long orderId;
    private String customerName;
    private String orderDate;
    private List<String> menuList;
    private int totalPrice;
    private int orderState;
}

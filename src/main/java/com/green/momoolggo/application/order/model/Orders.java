package com.green.momoolggo.application.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Orders {
    private long orderId;
    private long userNo;
    private int amount;
}
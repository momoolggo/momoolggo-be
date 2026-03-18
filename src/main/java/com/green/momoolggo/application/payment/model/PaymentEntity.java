package com.green.momoolggo.application.payment.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PaymentEntity {
    private long   paymentId;
    private String orderId;
    private String paymentKey;
    private int    amount;
    private int    payState;
}
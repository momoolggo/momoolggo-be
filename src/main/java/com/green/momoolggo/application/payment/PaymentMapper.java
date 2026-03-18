package com.green.momoolggo.application.payment;

import com.green.momoolggo.application.payment.model.PaymentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    void insertPayment(PaymentEntity payment);
}
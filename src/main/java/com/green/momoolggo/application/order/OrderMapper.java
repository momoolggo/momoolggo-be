package com.green.momoolggo.application.order;

import com.green.momoolggo.application.order.model.OrderAddressInfo;
import com.green.momoolggo.application.order.model.OrderReqDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    // 유저 전화번호 조회
    String findTelByUserNo(@Param("userNo") Long userNo);

    // 기본 배송지 조회
    OrderAddressInfo findDefaultAddress(@Param("userNo") Long userNo);

    // 주문 INSERT
    void insertOrder(@Param("userNo")    Long    userNo,
                     @Param("storeId")   Long    storeId,
                     @Param("request")   String  request,
                     @Param("riderReq")  String  riderRequest,
                     @Param("address")   String  address,
                     @Param("addressDetail") String addressDetail,
                     @Param("deliveryFee")   Integer deliveryFee,
                     @Param("amount")    Integer amount,
                     @Param("payState")  Integer payState);

    // 방금 INSERT한 order_id
    Long getLastOrderId();

    // 주문 상세 INSERT
    void insertOrderDetail(@Param("orderId")    Long    orderId,
                           @Param("menuId")     Long    menuId,
                           @Param("quantity")   Integer quantity,
                           @Param("menuName")   String  menuName,
                           @Param("menuPrice")  Integer menuPrice);
}
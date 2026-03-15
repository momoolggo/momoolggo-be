package com.green.momoolggo.application.cart.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartListRes {
    private Long cartId;
    private Long storeId;
    private String storeName;
    private List<CartItemRes> items;
}

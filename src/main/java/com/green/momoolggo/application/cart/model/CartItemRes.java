package com.green.momoolggo.application.cart.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRes {
    private Long id;          // cart_detail.cart_item_id
    private String menuName;
    private Integer price;
    private Integer quantity;
    private String menuPic;
}

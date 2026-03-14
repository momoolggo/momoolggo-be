package com.green.momoolggo.application.cart;

import com.green.momoolggo.application.cart.model.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {

    // menuId → menu_category 조인으로 storeId 조회
    Long findStoreIdByMenuId(@Param("menuId") Long menuId);

    // userNo로 현재 장바구니 조회
    Cart findCartByUserNo(@Param("userNo") Long userNo);

    // 장바구니 생성
    void insertCart(@Param("userNo") Long userNo, @Param("storeId") Long storeId);

    // 마지막 생성된 cart_id 조회
    Long getLastCartId();

    // 장바구니 아이템 추가
    void insertCartItem(@Param("cartId") Long cartId,
                        @Param("menuId") Long menuId,
                        @Param("quantity") int quantity);

    // 장바구니 아이템 전체 삭제
    void deleteAllCartItems(@Param("cartId") Long cartId);

    // 장바구니 삭제
    void deleteCart(@Param("cartId") Long cartId);
}
package com.green.momoolggo.application.cart;

import com.green.momoolggo.application.cart.model.Cart;
import com.green.momoolggo.application.cart.model.CartAddRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    @Transactional
    public void addToCart(CartAddRequestDto dto) {
        // 1. menuId로 storeId 조회
        Long storeId = cartMapper.findStoreIdByMenuId(dto.getMenuId());
        if (storeId == null) {
            throw new RuntimeException("존재하지 않는 메뉴입니다.");
        }

        // 2. 기존 장바구니 조회
        Cart existCart = cartMapper.findCartByUserNo(dto.getUserNo());

        if (existCart == null) {
            // 3-A. 장바구니 없음 → 새로 생성
            cartMapper.insertCart(dto.getUserNo(), storeId);
            Long newCartId = cartMapper.getLastCartId();
            cartMapper.insertCartItem(newCartId, dto.getMenuId(), dto.getQuantity());

        } else if (existCart.getStoreId().equals(storeId)) {
            // 3-B. 같은 매장 → 바로 아이템 추가
            cartMapper.insertCartItem(existCart.getCartId(), dto.getMenuId(), dto.getQuantity());

        } else {
            // 3-C. 다른 매장 → 409 반환 (프론트에서 confirm 후 clearAndAdd 호출)
            throw new DifferentStoreException("다른 매장의 메뉴가 장바구니에 있습니다.");
        }
    }

    @Transactional
    public void clearAndAddToCart(CartAddRequestDto dto) {
        // 기존 장바구니 전체 비우고 새로 담기
        Long storeId = cartMapper.findStoreIdByMenuId(dto.getMenuId());

        Cart existCart = cartMapper.findCartByUserNo(dto.getUserNo());
        if (existCart != null) {
            cartMapper.deleteAllCartItems(existCart.getCartId());
            cartMapper.deleteCart(existCart.getCartId());
        }

        cartMapper.insertCart(dto.getUserNo(), storeId);
        Long newCartId = cartMapper.getLastCartId();
        cartMapper.insertCartItem(newCartId, dto.getMenuId(), dto.getQuantity());
    }
}
package com.green.momoolggo.application.cart;

import com.green.momoolggo.application.cart.model.CartAddRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 담기
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartAddRequestDto dto) {
        try {
            cartService.addToCart(dto);
            return ResponseEntity.ok(Map.of("result", "success"));
        } catch (DifferentStoreException e) {
            // 다른 매장 → 프론트에서 confirm 팝업 띄우도록 409 반환
            return ResponseEntity.status(409).body(Map.of(
                    "result", "differentStore",
                    "message", e.getMessage()
            ));
        }
    }

    // 장바구니 비우고 새로 담기
    @PostMapping("/clear-and-add")
    public ResponseEntity<?> clearAndAdd(@RequestBody CartAddRequestDto dto) {
        cartService.clearAndAddToCart(dto);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
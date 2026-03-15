package com.green.momoolggo.application.order;

import com.green.momoolggo.application.order.model.OrderInfoRes;
import com.green.momoolggo.application.order.model.OrderReqDto;
import com.green.momoolggo.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 화면 초기 데이터 조회
    @GetMapping
    public ResponseEntity<?> getOrderInfo(
            @AuthenticationPrincipal UserPrincipal principal) {
        OrderInfoRes res = orderService.getOrderInfo(principal.getSignedUserNo());
        return ResponseEntity.ok(Map.of("resultData", res));
    }

    // 주문 확정
    @PostMapping
    public ResponseEntity<?> placeOrder(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody OrderReqDto dto) {
        orderService.placeOrder(principal.getSignedUserNo(), dto);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
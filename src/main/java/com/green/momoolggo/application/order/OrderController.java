package com.green.momoolggo.application.order;

import com.green.momoolggo.application.order.model.OrderHistoryDto;
import com.green.momoolggo.application.order.model.OrderHistoryReq;
import com.green.momoolggo.application.order.model.OrderInfoRes;
import com.green.momoolggo.application.order.model.OrderReqDto;
import com.green.momoolggo.configuration.model.ResultResponse;
import com.green.momoolggo.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        long orderId = orderService.placeOrder(principal.getSignedUserNo(), dto);
        return ResponseEntity.ok(Map.of("result", "success","orderId", orderId));
    }

    @DeleteMapping("/{id}")
    public ResultResponse<?> deleteOrder(@PathVariable  long id){
        int result= orderService.deleteOrder(id);
        return new ResultResponse<>(result==1 ? "삭제성공": "삭제실패", "dd");
    }

    //주문내역
    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistory(@ModelAttribute OrderHistoryReq req) {
        return ResponseEntity.ok(orderService.getOrderHistory(req));
    }

    //주문상세
    @GetMapping("/history/{id}")
    public ResponseEntity<OrderHistoryDto> orderHistoryDetail(@PathVariable long id){
        return ResponseEntity.ok(orderService.orderHistoryDetail(id));
    }
}

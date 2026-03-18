package com.green.momoolggo.application.payment;

import com.green.momoolggo.application.cart.CartMapper;
import com.green.momoolggo.application.order.OrderMapper;
import com.green.momoolggo.application.payment.model.PaymentConfirmReq;
import com.green.momoolggo.application.payment.model
        .PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.green.momoolggo.application.cart.model.Cart;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private static final String SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"; // 🔑 실제키로 교체

    public void confirmPayment(PaymentConfirmReq req) throws Exception {

        // 1. 토스 승인 API 호출
        JSONObject requestBody = new JSONObject();
        requestBody.put("paymentKey", req.getPaymentKey());
        requestBody.put("orderId",    req.getOrderId());
        requestBody.put("amount",     req.getAmount());

        String encoded = Base64.getEncoder()
                .encodeToString((SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + encoded);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = connection.getResponseCode();
        InputStream responseStream = code == 200
                ? connection.getInputStream()
                : connection.getErrorStream();

        JSONParser parser = new JSONParser();
        JSONObject response;
        try (Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            response = (JSONObject) parser.parse(reader);
        }
        if (code == 200) {
            // orderId로 주문 정보를 조회해 유저 번호(userNo) 알아내기
            // (주의: orderId가 String이면 Long으로 파싱 필요)
            Long orderId = Long.parseLong(req.getOrderId());
            Long userNo = orderMapper.findUserNoByOrderId(orderId);

            if (userNo != null) {
                // 3. 유저 번호로 장바구니 찾기
                Cart cart = cartMapper.findCartEntityByUserNo(userNo);

                if (cart != null) {
                    // 4. 이제 안전하게 cartId를 사용할 수 있습니다!
                    cartMapper.deleteAllCartItems(cart.getCartId());
                    cartMapper.deleteCart(cart.getCartId());
                }
            }
        }
        // 2. 실패 시 예외 처리
        if (code != 200) {
            throw new RuntimeException((String) response.get("message"));
        }

        // 3. ✅ payment 테이블에 저장
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(req.getOrderId());
        payment.setPaymentKey(req.getPaymentKey());
        payment.setAmount(req.getAmount());
        payment.setPayState(req.getPayState());
        paymentMapper.insertPayment(payment);
    }
}
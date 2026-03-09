package com.green.momoolggo.application.address;

import com.green.momoolggo.application.address.model.AddressSearchRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressSearchService {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public List<AddressSearchRes> search(String query) {
        String url = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode")
                .queryParam("query", query)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        ResponseEntity<Map> response = new RestTemplate()
                .exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

        List<Map<String, Object>> addresses =
                (List<Map<String, Object>>) response.getBody().get("addresses");

        List<AddressSearchRes> result = new ArrayList<>();
        if (addresses != null) {
            for (Map<String, Object> addr : addresses) {
                result.add(new AddressSearchRes(
                        (String) addr.get("roadAddress"),
                        (String) addr.get("jibunAddress"),
                        Double.parseDouble((String) addr.get("y")),  // 위도
                        Double.parseDouble((String) addr.get("x"))   // 경도
                ));
            }
        }
        return result;
    }
}
package com.green.momoolggo.application.address;

import com.green.momoolggo.application.address.model.AddressSearchRes;
import com.green.momoolggo.configuration.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressSearchController {

    private final AddressSearchService addressSearchService;

    // ── 주소 검색 GET /api/address/search?query=xxx
    @GetMapping("/search")
    public ResultResponse<List<AddressSearchRes>> search(@RequestParam String query) {
        return new ResultResponse<>("주소 검색 성공", addressSearchService.search(query));
    }
}
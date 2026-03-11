package com.green.momoolggo.application.store;

import com.green.momoolggo.application.store.model.StoreGetReq;
import com.green.momoolggo.application.store.model.StoreGetRes;
import com.green.momoolggo.configuration.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping
    public ResultResponse<?> StoreListGet(@ModelAttribute StoreGetReq req){
        List<StoreGetRes> result = storeService.storeListGet(req);
            System.out.println(result);
        return new ResultResponse<>("ㅇ", result);
    }

    @GetMapping("/{id}")
    public ResultResponse<?> StoreOneGet(@ModelAttribute StoreGetReq req){
        return null;
    }

    @GetMapping("/menu")
    public ResultResponse<?> MenuListGet(@ModelAttribute StoreGetReq req){
        return null;
    }

}


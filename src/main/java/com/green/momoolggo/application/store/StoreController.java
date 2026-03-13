package com.green.momoolggo.application.store;

import com.green.momoolggo.application.store.model.*;
import com.green.momoolggo.configuration.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping  //가게 전체목록
    public ResultResponse<?> StoreListGet(@ModelAttribute StoreGetReq req){
        List<StoreGetRes> result = storeService.storeListGet(req);
            System.out.println(result);
        return new ResultResponse<>("ㅇ", result);
    }

    @GetMapping("/{id}") //가게 상세정보
    public ResultResponse<?> StoreOneGet(@PathVariable long id){
        StoreOneGetRes result = storeService.storeOneGet(id);
        return new ResultResponse<>("",result);
    }

    @GetMapping("/menu/{id}") //가게 메뉴목록
    public ResultResponse<?> MenuListGet(@PathVariable long id){
        List<MenuGetRes> result= storeService.menuListGet(id);
        return new ResultResponse<>("", result);
    }

    @GetMapping("/favorite") //찜한 가게 목록
    public ResultResponse<?> wishListGet(@ModelAttribute StoreFavoriteReq req) {
        Map<String, Object> result = storeService.getWishListResponse(req);

        return new ResultResponse<>("찜 목록 조회 성공", result);
    }
}


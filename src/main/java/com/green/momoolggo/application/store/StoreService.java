package com.green.momoolggo.application.store;

import com.green.momoolggo.application.store.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreMapper storeMapper;

    public List<StoreGetRes> storeListGet(StoreGetReq req){
        return storeMapper.findAll(req);
    }

    public StoreOneGetRes storeOneGet(long id){
        return storeMapper.findOne(id);
    }

    public List<MenuGetRes> menuListGet(long id){ return storeMapper.menuAll(id); }

    public Map<String, Object> getWishListResponse(StoreFavoriteReq req) {
        Map<String, Object> response = new HashMap<>();
        // 1. 찜 목록 리스트 가져오기 (LIMIT 적용됨)
        List<StoreGetRes> list = storeMapper.favoriteList(req);
        // 2. 전체 찜 개수 가져오기 (LIMIT 없음)
        int totalCount = storeMapper.favoriteCount(req.getUserNo());

        response.put("list", list);
        response.put("totalCount", totalCount);
        return response;
    }

    public List<StoreGetRes> storeSearchList(@Param("searchText") String searchText) {
        if( searchText == null || searchText.trim().isEmpty()) {
            return List.of();

        }
        return storeMapper.searchStore(searchText);}

}

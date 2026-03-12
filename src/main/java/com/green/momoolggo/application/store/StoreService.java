package com.green.momoolggo.application.store;

import com.green.momoolggo.application.store.model.StoreGetReq;
import com.green.momoolggo.application.store.model.StoreGetRes;
import com.green.momoolggo.application.store.model.StoreOneGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreMapper storeMapper;

    public List<StoreGetRes> storeListGet(StoreGetReq req){
        return storeMapper.findAll(req);
    }

    public StoreOneGetRes storeOneGet(long req){
        return storeMapper.findOne(req);
    }
}

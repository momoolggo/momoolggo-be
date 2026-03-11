package com.green.momoolggo.application.store;

import com.green.momoolggo.application.store.model.StoreGetReq;
import com.green.momoolggo.application.store.model.StoreGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper

public interface StoreMapper {
List<StoreGetRes> findAll(StoreGetReq req);
}

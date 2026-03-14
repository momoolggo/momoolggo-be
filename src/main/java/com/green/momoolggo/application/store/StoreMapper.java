package com.green.momoolggo.application.store;

import com.green.momoolggo.application.store.model.MenuGetRes;
import com.green.momoolggo.application.store.model.StoreGetReq;
import com.green.momoolggo.application.store.model.StoreGetRes;
import com.green.momoolggo.application.store.model.StoreOneGetRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper

public interface StoreMapper {
    List<StoreGetRes> findAll(StoreGetReq req);
    StoreOneGetRes findOne(long id);
    List<MenuGetRes> menuAll(long id);
    List<StoreGetRes> searchStore(@org.apache.ibatis.annotations.Param("searchText") String searchText);
}

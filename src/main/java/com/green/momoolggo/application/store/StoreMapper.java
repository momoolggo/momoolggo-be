package com.green.momoolggo.application.store;

import com.green.momoolggo.application.store.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper

public interface StoreMapper {
    List<StoreGetRes> findAll(StoreGetReq req);
    StoreOneGetRes findOne(long id);
    List<MenuGetRes> menuAll(long id);
    List<StoreGetRes> favoriteList( StoreFavoriteReq req);
    int favoriteCount(long id);
    int checkWish(FavoriteToggleReq req);
    int deleteWish(FavoriteToggleReq req);
    int insertWish(FavoriteToggleReq req);
}

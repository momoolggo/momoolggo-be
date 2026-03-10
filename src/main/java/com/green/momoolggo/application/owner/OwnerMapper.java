package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OwnerMapper {

    // 가게 등록
    int registerStore(OwnerStoreRegReq dto);

    // 가게 기본 정보 수정
    int updateStore(OwnerStoreUpdateReq dto);

    // 가게 운영정보 수정(서비스 사용)
    int updateStoreStatus(OwnerStoreUpdateStatusReq dto);
    // 가게 정보 조회(수정 후 최신 데이터 가져올 때->반영)
    OwnerStoreRes getStoreById(Long storeId);

    // 가게 삭제
    int deleteStore(Long storeId);

    // 주문 목록 조회
    List<OwnerOrderRes> getOrders(@Param("storeId") Long storeId, @Param("state") Integer state);

    // 주문 상태 수정
    int updateOrderState(OwnerOrderStateReq req);
}

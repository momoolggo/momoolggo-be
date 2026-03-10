package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.OwnerStoreRegReq;
import com.green.momoolggo.application.owner.model.OwnerStoreUpdateReq;
import com.green.momoolggo.application.owner.model.OwnerStoreUpdateStatusReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OwnerMapper {

    // 가게 등록
    int registerStore(OwnerStoreRegReq dto);

    // 가게 기본 정보 수정
    int updateStore(OwnerStoreUpdateReq dto);

    // 가게 운영정보 수정
    int updateStoreStatus (OwnerStoreUpdateStatusReq dto);

    // 가게 삭제
    int deleteStore(Long storeId);


}

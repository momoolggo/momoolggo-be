package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.OwnerStoreRegReq;
import com.green.momoolggo.application.owner.model.OwnerStoreUpdateReq;
import com.green.momoolggo.application.owner.model.OwnerStoreUpdateStatusReq;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;

    // 가게 등록
    public void registerStore(OwnerStoreRegReq dto){
        log.info("가게 등록 로직 시작: {}", dto.getStoreName());
        int result = ownerMapper.registerStore(dto);
    }

    // 가게 기본 정보 수정
    public void updateStore(OwnerStoreUpdateReq dto){
        int result = ownerMapper.updateStore(dto);
        if (result == 0){
            throw new RuntimeException("가게 정보 수정 실패: 해당 가게를 찾을 수 없음");
        }
    }

    // 가게 운영정보 수정
    public void updateStoreStatus(OwnerStoreUpdateStatusReq dto){
        int result = ownerMapper.updateStoreStatus(dto);
        if (result == 0){
            throw new RuntimeException("운영 정보 수정 실패: 해당 가게를 찾을 수 없음");
        }
    }

    // 가게 삭제
    public void deleteStore(Long store_id){
        int result = ownerMapper.deleteStore(store_id);
        if (result == 0){
            throw new RuntimeException("삭제할 가게를 찾을 수 없습니다.");
        }
    }



}

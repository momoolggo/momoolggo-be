package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.PanelUI;
import java.util.List;

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
    @Transactional
    public OwnerStoreRes updateStoreStatus(OwnerStoreUpdateStatusReq dto){
        ownerMapper.updateStoreStatus(dto);
        return ownerMapper.getStoreById(dto.getStoreId());
    }

    // 가게 삭제
    public void deleteStore(Long store_id){
        int result = ownerMapper.deleteStore(store_id);
        if (result == 0){
            throw new RuntimeException("삭제할 가게를 찾을 수 없습니다.");
        }
    }

    // 가게 주문 조회
    public List<OwnerOrderRes> getOrders(Long storedId, Integer state){
        return ownerMapper.getOrders(storedId, state);
    }

    // 주문 상태 수정

    public void updateOrderState(OwnerOrderStateReq req){
        int result = ownerMapper.updateOrderState(req);
        if (result == 0){
            throw new RuntimeException("주문 상태 변경 실패: 주문을 찾을 수 없습니다.");
        }
    }

    // 가게 메뉴 등록
    public OwnerMenuRes registerMenu(OwnerMenuRegReq dto){
        ownerMapper.registerMenu(dto);
        return ownerMapper.getMenuById(dto.getMenuId());
    }

    // 가게 메뉴 수정
    @Transactional
    public OwnerMenuRes updateMenu(OwnerMenuUpdateReq dto){
        int result = ownerMapper.updateMenu(dto);
        if (result == 0) {
            throw new RuntimeException("메뉴 수정 실패: 해당 메뉴를 찾을 수 없음");
        }
        return ownerMapper.getMenuById(dto.getMenuId());
    }

    // 가게 메뉴 삭제
    @Transactional
    public Long deleteMenu(Long menuId){
        int result = ownerMapper.deleteMenu(menuId);
        if (result == 0) {
            throw new RuntimeException("메뉴 삭제 실패: 해당 메뉴를 찾을 수 없음");
        }
        return menuId;
    }


}

package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.OwnerStoreRegReq;
import com.green.momoolggo.application.owner.model.OwnerStoreUpdateReq;
import com.green.momoolggo.application.owner.model.OwnerStoreUpdateStatusReq;
import com.green.momoolggo.configuration.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    // 가게 등록
    @PostMapping("/store")
    public ResultResponse<Void> postStore(@RequestBody OwnerStoreRegReq dto){
        log.info("가게 등록 요청 데이터: {}", dto);
        ownerService.registerStore(dto);
        return new ResultResponse<>("가게 등록 성공", null);
    }

    // 가게 기본정보 수정(가게명, 번호, 주소, 사업자 번호 등등)
    @PutMapping("/store")
    public ResultResponse<Void> updatedStore(@RequestBody OwnerStoreUpdateReq dto){
        log.info("가게 기본 정보 수정: {}", dto);
        ownerService.updateStore(dto);
        return new ResultResponse<>("기본정보 수정 완료", null);
    }

    // 가게 운영정보 수정(영업상태, 휴무일, 공지 등)
    @PutMapping("/store/status")
    public ResultResponse<Void> updateStoreStatus(@RequestBody OwnerStoreUpdateStatusReq dto){
        log.info("가게 운영관리 수정: {}", dto);
        ownerService.updateStoreStatus(dto);
        return new ResultResponse<>("운영정보 업데이트 완료", null);
    }

    //가게 삭제
    @DeleteMapping("/store/{store_id}")
    public ResultResponse<Void> deleteStore(@PathVariable Long store_id){
        log.info("가게 삭제 요청: store_id = {}", store_id);
        ownerService.deleteStore(store_id);
        return new ResultResponse<>("가게 삭제 성공", null);
    }

}

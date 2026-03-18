package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.PanelUI;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;

    // 가게 등록
    public void registerStore(OwnerStoreRegReq dto){
        log.info("가게 등록 로직 시작: {}", dto.getStoreName());
        log.info("상세주소: {}", dto.getAddressDetail());
        int result = ownerMapper.registerStore(dto);
        if (result == 0) {
            throw new RuntimeException("가게 등록 실패");
        }
        ownerMapper.registerStoreCategory(dto.getUserId(), dto.getCategoryId());
        ownerMapper.registerDefaultMenuCategory(dto.getUserId());
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

    //로그인할 때 가게 불러오기
    public OwnerStoreRes getMyStore(long ownerNo) {
        return ownerMapper.getMyStore(ownerNo);
    }

    //매출관리
    public OwnerSalesStatsRes getSalesStats(long storeId, String period) {
        return ownerMapper.getSalesStats(storeId, period);
    }

    public List<OwnerSalesRankingRes> getSalesRanking(long storeId, String period) {
        return ownerMapper.getSalesRanking(storeId, period);
    }

    public List<OwnerMenuRes> getMenusByStoreId(Long storeId) {
        return ownerMapper.getMenusByStoreId(storeId);
    }

    //메뉴 카테고리 관련
    public List<Map<String, Object>> getCategoriesByStoreId(Long storeId) {
        return ownerMapper.getCategoriesByStoreId(storeId);
    }

    public void addCategory(Long storeId, String category) {
        ownerMapper.addCategory(storeId, category);
    }

    public void updateCategory(Long categoryId, String category) {
        ownerMapper.updateCategory(categoryId, category);
    }

    public void deleteCategory(Long categoryId) {
        ownerMapper.deleteCategory(categoryId);
    }

    public String uploadMenuImage(MultipartFile file, String uploadPath) {
        try {
            // 저장 폴더 생성
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            // 파일명 중복 방지
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File savedFile = new File(uploadPath + fileName);

            // 압축해서 저장 (30MB → 약 1MB 이하)
            Thumbnails.of(file.getInputStream())
                    .size(800, 600)
                    .outputQuality(0.8)
                    .toFile(savedFile);

            return "/uploads/menu/" + fileName;  // DB에 저장될 경로

        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패");
        }
    }

}

package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OwnerMapper {

    // 가게 등록
    int registerStore(OwnerStoreRegReq dto);

    // 가게 기본 정보 수정
    int updateStore(OwnerStoreUpdateReq dto);

    // 가게 운영정보 수정
    int updateStoreStatus(OwnerStoreUpdateStatusReq dto);

    // 가게 정보 조회
    OwnerStoreRes getStoreById(Long storeId);

    // ★ 가게 이미지 수정 (추가)
    int updateStoreImage(@Param("storeId") Long storeId, @Param("imageData") String imageData);

    // 가게 삭제
    int deleteStore(Long storeId);

    // 주문 목록 조회
    List<OwnerOrderRes> getOrders(@Param("storeId") Long storeId,
                                  @Param("state") Integer state,
                                  @Param("date") String date);  // ← 추가

    // 주문 상태 수정
    int updateOrderState(OwnerOrderStateReq req);

    // 메뉴등록
    int registerMenu(OwnerMenuRegReq dto);

    // 등록 메뉴 상세조회
    OwnerMenuRes getMenuById(Long menuId);

    // 메뉴 수정
    int updateMenu(OwnerMenuUpdateReq dto);

    // ★ 메뉴 이미지 수정 (추가)
    int updateMenuImage(@Param("menuId") Long menuId, @Param("imageData") String imageData);

    // 메뉴 삭제
    int deleteMenu(Long menuId);

    // 메뉴 카테고리 자동 생성
    void registerDefaultMenuCategory(long userId);

    // 가게 카테고리 설정
    void registerStoreCategory(@Param("userId") long userId, @Param("categoryId") long categoryId);

    // 로그인할 때 가게 불러오기
    OwnerStoreRes getMyStore(long ownerNo);

    // 매출관리
    OwnerSalesStatsRes getSalesStats(@Param("storeId") long storeId, @Param("period") String period);

    List<OwnerSalesRankingRes> getSalesRanking(@Param("storeId") long storeId, @Param("period") String period);

    // 메뉴 불러오기
    List<OwnerMenuRes> getMenusByStoreId(Long storeId);

    // 카테고리 관련
    List<Map<String, Object>> getCategoriesByStoreId(Long storeId);

    void addCategory(@Param("storeId") Long storeId, @Param("category") String category);

    void updateCategory(@Param("categoryId") Long categoryId, @Param("category") String category);

    void deleteCategory(Long categoryId);

    void deleteOrderDetail(Long orderId);

    void deleteOrder(Long orderId);
}
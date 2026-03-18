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

    // 가게 삭제
    int deleteStore(Long storeId);

    // 로그인할 때 가게 불러오기
    OwnerStoreRes getMyStore(long ownerNo);

    // ========== 주문 관련 ==========

    // 주문 목록 조회 (날짜 필터 추가)
    List<OwnerOrderRes> getOrders(@Param("storeId") Long storeId,
                                  @Param("state") Integer state,
                                  @Param("date") String date);

    // 주문 상태 수정
    int updateOrderState(OwnerOrderStateReq req);

    // 주문 상세 삭제 (order_detail 먼저)
    void deleteOrderDetail(Long orderId);

    // 주문 삭제
    void deleteOrder(Long orderId);

    // ========== 메뉴 관련 ==========

    // 메뉴 등록
    int registerMenu(OwnerMenuRegReq dto);

    // 등록 메뉴 상세조회
    OwnerMenuRes getMenuById(Long menuId);

    // 메뉴 수정
    int updateMenu(OwnerMenuUpdateReq dto);

    // 메뉴 삭제
    int deleteMenu(Long menuId);

    // 메뉴 목록 조회
    List<OwnerMenuRes> getMenusByStoreId(Long storeId);

    // ========== 카테고리 관련 ==========

    // 메뉴 카테고리 자동 생성
    void registerDefaultMenuCategory(long userId);

    // 가게 카테고리 설정
    void registerStoreCategory(@Param("userId") long userId, @Param("categoryId") long categoryId);

    List<Map<String, Object>> getCategoriesByStoreId(Long storeId);
    void addCategory(@Param("storeId") Long storeId, @Param("category") String category);
    void updateCategory(@Param("categoryId") Long categoryId, @Param("category") String category);
    void deleteCategory(Long categoryId);

    // ========== 매출 관련 ==========

    OwnerSalesStatsRes getSalesStats(@Param("storeId") long storeId, @Param("period") String period);
    List<OwnerSalesRankingRes> getSalesRanking(@Param("storeId") long storeId, @Param("period") String period);
}
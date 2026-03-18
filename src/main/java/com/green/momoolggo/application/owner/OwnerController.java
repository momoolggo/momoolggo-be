package com.green.momoolggo.application.owner;


import com.green.momoolggo.application.owner.model.*;
import com.green.momoolggo.configuration.model.ResultResponse;
import com.green.momoolggo.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import net.coobird.thumbnailator.Thumbnails;
import java.io.File;
import java.util.UUID;

import java.util.List;
import java.util.Map;

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
    public ResultResponse<OwnerStoreRes> updateStoreStatus(@RequestBody OwnerStoreUpdateStatusReq dto){
        log.info("가게 운영관리 수정: {}", dto);
        OwnerStoreRes updatedStore = ownerService.updateStoreStatus(dto);
        return new ResultResponse<>("운영정보 업데이트 완료", null);
    }

    //가게 삭제
    @DeleteMapping("/store/{store_id}")
    public ResultResponse<Void> deleteStore(@PathVariable Long store_id){
        log.info("가게 삭제 요청: store_id = {}", store_id);
        ownerService.deleteStore(store_id);
        return new ResultResponse<>("가게 삭제 성공", null);
    }

    //------------------------------------------------------------------------------------------------

    // 가게 주문 조회
    @ GetMapping("/order")
    public ResultResponse<List<OwnerOrderRes>> getOrders(@RequestParam Long store_id,
                                                         @RequestParam(required = false) Integer state){
        log.info("주문 조회 요청: state_id = {}, state = {}", store_id, state);
        List<OwnerOrderRes> list = ownerService.getOrders(store_id, state);
        return new ResultResponse<>(String.format("%d건의 주문을 조회합니다.", list.size()),list);
    }

    // 주문 상태 수정
    @PutMapping("/order/{order_id}")
    public ResultResponse<Void> putOrderState(@PathVariable Long order_id,
                                              @RequestBody OwnerOrderStateReq req){
        log.info("주문 상태 요청: order_id = {}, state = {}", order_id, req.getOrderState());
        req.setOrderId(order_id);
        ownerService.updateOrderState(req);
        return new ResultResponse<>("주문 상태 수정 성공", null);
    }

    //---------------------------------------------------------------------------------------------------

    // 가게 메뉴 등록
    @PostMapping("/menu")
    public ResultResponse<OwnerMenuRes> registerMenu(@RequestBody OwnerMenuRegReq dto){
        OwnerMenuRes result = ownerService.registerMenu(dto);
        return new ResultResponse<>("메뉴가 등록 되었습니다", result);
    }
    // 가게 메뉴 수정
    @PutMapping("/menu")
    public ResultResponse<OwnerMenuRes> updateMenu(@RequestBody OwnerMenuUpdateReq dto){
        OwnerMenuRes updateMenu = ownerService.updateMenu(dto);
        return new ResultResponse<>("메뉴가 수정되었습니다.", updateMenu);
    }
    // 가게 메뉴 삭제
    @DeleteMapping("/menu/{menu_id}")
    public ResultResponse<Long> deleteMenu(@PathVariable("menu_id") Long menuId){
        Long deleteId = ownerService.deleteMenu(menuId);
        return new ResultResponse<>("메뉴가 삭제되었습니다.", deleteId);
    }

    //로그인할 때 가게 불러오기
    @GetMapping("/store")
    public ResultResponse<OwnerStoreRes> getMyStore(@AuthenticationPrincipal UserPrincipal principal) {
        OwnerStoreRes store = ownerService.getMyStore(principal.getSignedUserNo());
        return new ResultResponse<>("가게 조회 성공", store);
    }

    //매출관리
    @GetMapping("/sales/stats")
    public ResultResponse<OwnerSalesStatsRes> getSalesStats(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam String period) {
        OwnerSalesStatsRes stats = ownerService.getSalesStats(principal.getSignedUserNo(), period);
        return new ResultResponse<>("매출 통계 조회 성공", stats);
    }

    @GetMapping("/sales/ranking")
    public ResultResponse<List<OwnerSalesRankingRes>> getSalesRanking(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam String period) {
        List<OwnerSalesRankingRes> ranking = ownerService.getSalesRanking(principal.getSignedUserNo(), period);
        return new ResultResponse<>("매출 순위 조회 성공", ranking);
    }

    // 가게 메뉴 목록 조회
    @GetMapping("/menu")
    public ResultResponse<List<OwnerMenuRes>> getMenus(@RequestParam Long storeId) {
        List<OwnerMenuRes> list = ownerService.getMenusByStoreId(storeId);
        return new ResultResponse<>("메뉴 조회 성공", list);
    }

    //메뉴 카테고리 관련
    @GetMapping("/category")
    public ResultResponse<List<Map<String, Object>>> getCategories(@RequestParam Long storeId) {
        return new ResultResponse<>("카테고리 조회 성공", ownerService.getCategoriesByStoreId(storeId));
    }

    @PostMapping("/category")
    public ResultResponse<Void> addCategory(@RequestBody Map<String, Object> body) {
        ownerService.addCategory(Long.valueOf(body.get("storeId").toString()), body.get("category").toString());
        return new ResultResponse<>("카테고리 추가 성공", null);
    }

    @PutMapping("/category")
    public ResultResponse<Void> updateCategory(@RequestBody Map<String, Object> body) {
        ownerService.updateCategory(Long.valueOf(body.get("categoryId").toString()), body.get("category").toString());
        return new ResultResponse<>("카테고리 수정 성공", null);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResultResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        ownerService.deleteCategory(categoryId);
        return new ResultResponse<>("카테고리 삭제 성공", null);
    }

    @Value("${file.upload.path}")
    private String uploadPath;

    // 이미지 업로드 API
    @PostMapping("/menu/image")
    public ResultResponse<String> uploadMenuImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = ownerService.uploadMenuImage(file, uploadPath);
        return new ResultResponse<>("이미지 업로드 성공", imageUrl);
    }
}

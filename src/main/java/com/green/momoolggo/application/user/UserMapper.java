package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    // 회원가입
    int signup(UserSignupReq req);

    // 로그인
    User findByUserId(String userId);

    // 아이디 중복확인
    int countByUserId(String userId);

    User findByUserNo(Long userNo);

    int update(UserUpdateReq req);

    int postReview(ReviewReq req);

    long checkReviewWriter(ReviewReq req);

    List<ReviewRes> getReviews(GetReviewReq req);
    int countReviews(long userNo);
    int deleteReview(@Param("userNo") long userNo, @Param("reviewId") long reviewId);

    // 리뷰 단건 조회
    Map<String, Object> getReviewById(long reviewId);

    // 리뷰 수정
    int updateReview(@Param("reviewId") long reviewId,
                     @Param("userNo") long userNo,
                     @Param("rating") int rating,
                     @Param("contents") String contents);

    // 가게별 리뷰 조회
    List<Map<String, Object>> getStoreReviews(long storeId);

    // 주문으로 가게 ID 조회 (별점 갱신용)
    long findStoreIdByOrderId(long orderId);

    // 리뷰로 가게 ID 조회 (별점 갱신용)
    long findStoreIdByReviewId(long reviewId);

    // 가게 별점/리뷰수 갱신
    void updateStoreRating(long storeId);
}
package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
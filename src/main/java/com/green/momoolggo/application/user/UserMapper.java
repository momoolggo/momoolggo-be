package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.User;
import com.green.momoolggo.application.user.model.UserSignupReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 회원가입
    int signup(UserSignupReq req);

    // 로그인
    User findByUserId(String userId);

    // 아이디 중복확인
    int countByUserId(String userId);
}
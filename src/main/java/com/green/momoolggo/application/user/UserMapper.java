package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.User;
import org.apache.ibatis.annotations.Mapper;

// MyBatis 매퍼 인터페이스
// 실제 SQL은 resources/mappers/user.xml 에 작성
@Mapper
public interface UserMapper {

    // 로그인 — 아이디로 유저 정보 조회
    // 비밀번호 일치 여부 검증은 UserService 에서 처리
    User findByUserId(String userId);
}
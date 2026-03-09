package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.User;
import com.green.momoolggo.application.user.model.UserSigninReq;
import com.green.momoolggo.application.user.model.UserSigninRes;
import com.green.momoolggo.application.user.model.UserSignupReq;
import com.green.momoolggo.configuration.model.JwtUser;
import com.green.momoolggo.configuration.security.JwtTokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    // ── 아이디 중복확인
    public boolean checkId(String userId) {
        return userMapper.countByUserId(userId) == 0;
    }

    // ── 회원가입
    public void signup(UserSignupReq req) {
        // 비밀번호 암호화
        req.setUserPw(passwordEncoder.encode(req.getUserPw()));
        // role 기본값 처리
        if (req.getRole() == null || req.getRole().isBlank()) {
            req.setRole("CUSTOMER");
        }
        userMapper.signup(req);
    }

    // ── 로그인
    public UserSigninRes signin(UserSigninReq req, HttpServletResponse res) {
        User user = userMapper.findByUserId(req.getUserId());
        if (user == null) {
            throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
        }
        if (!passwordEncoder.matches(req.getUserPw(), user.getUserPw())) {
            throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
        }
        JwtUser jwtUser = new JwtUser(user.getUserNo(), user.getRole(), null, user.getName());
        jwtTokenManager.issue(res, jwtUser);
        return new UserSigninRes(user.getUserNo(), user.getName(), user.getRole());
    }

    // ── 로그아웃
    public void signout(HttpServletResponse res) {
        jwtTokenManager.signOut(res);
    }
}
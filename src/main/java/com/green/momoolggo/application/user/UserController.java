package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.UserSigninReq;
import com.green.momoolggo.application.user.model.UserSigninRes;
import com.green.momoolggo.application.user.model.UserSignupReq;
import com.green.momoolggo.configuration.model.ResultResponse;
import com.green.momoolggo.configuration.model.UserPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ── 아이디 중복확인 GET /api/user/check-id?userId=xxx
    @GetMapping("/check-id")
    public ResultResponse<Void> checkId(@RequestParam String userId) {
        boolean available = userService.checkId(userId);
        if (!available) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }
        return new ResultResponse<>("사용 가능한 아이디입니다.", null);
    }

    // ── 회원가입 POST /api/user/join
    @PostMapping("/join")
    public ResultResponse<Void> signup(@RequestBody UserSignupReq req) {
        userService.signup(req);
        return new ResultResponse<>("회원가입 성공", null);
    }

    // ── 로그인 POST /api/user/login
    @PostMapping("/login")
    public ResultResponse<UserSigninRes> signin(@RequestBody UserSigninReq req,
                                                HttpServletResponse res) {
        UserSigninRes data = userService.signin(req, res);
        return new ResultResponse<>("로그인 성공", data);
    }

    // ── 로그아웃 POST /api/user/logout
    @PostMapping("/logout")
    public ResultResponse<Void> signout(HttpServletResponse res) {
        userService.signout(res);
        return new ResultResponse<>("로그아웃 완료", null);
    }

    // ── 내 정보 조회 GET /api/user/me
    @GetMapping("/me")
    public ResultResponse<UserSigninRes> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return new ResultResponse<>("로그인이 필요합니다.", null);
        }
        UserSigninRes data = new UserSigninRes(principal.getSignedUserNo(), principal.getName(), principal.getRole());
        return new ResultResponse<>("조회 성공", data);
    }
}
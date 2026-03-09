package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.UserSigninReq;
import com.green.momoolggo.application.user.model.UserSigninRes;
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

    /**
     * 로그인
     * POST /api/user/login
     * 인증 불필요 (WebSecurityConfiguration 에서 permitAll 처리)
     * 성공 시 AT/RT 가 쿠키에 자동으로 담겨서 응답됨
     */
    @PostMapping("/login")
    public ResultResponse<UserSigninRes> signin(@RequestBody UserSigninReq req,
                                                HttpServletResponse res) {
        UserSigninRes data = userService.signin(req, res);
        return new ResultResponse<>("로그인 성공", data);
    }

    /**
     * 로그아웃
     * POST /api/user/logout
     * 쿠키에서 AT, RT 를 삭제함
     */
    @PostMapping("/logout")
    public ResultResponse<Void> signout(HttpServletResponse res) {
        userService.signout(res);
        return new ResultResponse<>("로그아웃 완료", null);
    }

    /**
     * 내 정보 조회
     * GET /api/user/me
     * 프론트에서 로그인 후 역할에 따라 화면을 분기할 때 사용
     *
     * @AuthenticationPrincipal : TokenAuthenticationFilter 에서 SecurityContext 에 저장한
     *                            UserPrincipal 객체를 자동으로 꺼내서 주입해줌
     *                            (= 현재 로그인한 유저 정보)
     */
    @GetMapping("/me")
    public ResultResponse<UserSigninRes> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            // 쿠키에 AT 가 없거나 만료된 경우
            return new ResultResponse<>("로그인이 필요합니다.", null);
        }
        // UserSigninRes 재사용 (name 은 JWT 에 담지 않았으므로 null)
        UserSigninRes data = new UserSigninRes(
                principal.getSignedUserId(),
                null,
                principal.getRole()
        );
        return new ResultResponse<>("조회 성공", data);
    }
}
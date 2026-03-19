package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.*;
import com.green.momoolggo.configuration.constants.ConstJwt;
import com.green.momoolggo.configuration.model.JwtUser;
import com.green.momoolggo.configuration.model.ResultResponse;
import com.green.momoolggo.configuration.model.UserPrincipal;
import com.green.momoolggo.configuration.security.JwtTokenManager;
import com.green.momoolggo.configuration.security.JwtTokenProvider;
import com.green.momoolggo.configuration.util.MyCookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MyCookieUtil myCookieUtil;
    private final ConstJwt constJwt;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenManager jwtTokenManager;


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
        UserSigninRes data = new UserSigninRes(
                principal.getSignedUserNo(),
                principal.getName(),
                principal.getRole(),
                0L,  // ← me는 만료시각 안 씀, 0으로 채우기
                null
        );
        return new ResultResponse<>("조회 성공", data);
    }

    // 내 정보 조회
    @GetMapping
    public ResultResponse<UserGetRes> getUser(@AuthenticationPrincipal UserPrincipal principal) {
        UserGetRes data = userService.getUser(principal.getSignedUserNo());
        return new ResultResponse<>("조회 성공", data);
    }

    // 내 정보 수정
    @PutMapping
    public ResultResponse<Void> updateUser(@AuthenticationPrincipal UserPrincipal principal,
                                           @RequestBody UserUpdateReq req) {
        userService.updateUser(principal.getSignedUserNo(), req);
        return new ResultResponse<>("수정 성공", null);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest req, HttpServletResponse res) {
        // RT 쿠키에서 꺼내기
        String refreshToken = myCookieUtil.getValue(req, constJwt.getRefreshTokenCookieName());
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(new ResultResponse<>("RT 없음", null));
        }

        // RT 검증 & JwtUser 추출
        JwtUser jwtUser = jwtTokenProvider.getJwtUserFromToken(refreshToken);

        // 새 AT만 발급 (RT는 그대로)
        jwtTokenManager.setAccessTokenInCookie(res, jwtUser);

        return ResponseEntity.ok(new ResultResponse<>("AT 재발급 성공", null));
    }

    //리뷰 관련
    @PostMapping("/review")
    public void postReview(@AuthenticationPrincipal UserPrincipal userPrincipal ,@RequestBody ReviewReq req){
        userService.postReview(userPrincipal.getSignedUserNo(),req);
    }

    @GetMapping("/review")
    public ResponseEntity<Map<String, Object>> getReviews(
            @AuthenticationPrincipal UserPrincipal principal,
            @ModelAttribute GetReviewReq req) {
        req.setUserNo(principal.getSignedUserNo());
        return ResponseEntity.ok(userService.getReviews(req));
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable long reviewId) {
        userService.deleteReview(principal.getSignedUserNo(), reviewId);
        return ResponseEntity.ok(Map.of("result", "삭제성공"));
    }

    // 리뷰 단건 조회 (수정 시 기존 데이터 불러오기)
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable long reviewId) {
        return ResponseEntity.ok(userService.getReviewById(reviewId));
    }

    // 리뷰 수정
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable long reviewId,
            @RequestBody Map<String, Object> body) {
        int rating = Integer.parseInt(body.get("rating").toString());
        String contents = body.get("contents").toString();
        userService.updateReview(principal.getSignedUserNo(), reviewId, rating, contents);
        return ResponseEntity.ok(Map.of("result", "수정성공"));
    }
}
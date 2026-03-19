package com.green.momoolggo.application.user;

import com.green.momoolggo.application.address.UserAddressMapper;
import com.green.momoolggo.application.address.model.UserAddressReq;
import com.green.momoolggo.application.user.model.*;
import com.green.momoolggo.configuration.constants.ConstJwt;
import com.green.momoolggo.configuration.model.JwtUser;
import com.green.momoolggo.configuration.security.JwtTokenManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;
    private final UserAddressMapper userAddressMapper;
    private final ConstJwt constJwt;

    // ── 아이디 중복확인
    public boolean checkId(String userId) {
        return userMapper.countByUserId(userId) == 0;
    }

    // ── 회원가입
    @Transactional
    public void signup(UserSignupReq req) {
        req.setUserPw(passwordEncoder.encode(req.getUserPw()));
        if (req.getRole() == null || req.getRole().isBlank()) {
            req.setRole("CUSTOMER");
        }
        userMapper.signup(req);

        // 주소가 있으면 address 테이블에도 저장
        if (req.getAddress() != null && !req.getAddress().isBlank()) {
            UserAddressReq addressReq = new UserAddressReq();
            addressReq.setUserNo(req.getUserNo());
            addressReq.setAddress(req.getAddress());
            addressReq.setAddressDetail(req.getAddressDetail());
            addressReq.setLat(req.getLat());
            addressReq.setLng(req.getLng());
            addressReq.setDefaultAd(1);  // 첫 주소는 기본주소
            userAddressMapper.save(addressReq);
        }
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
        return new UserSigninRes(user.getUserNo(), user.getName(), user.getRole(),
                System.currentTimeMillis() + constJwt.getAccessTokenValidityMilliseconds(), null);
    }

    // ── 로그아웃
    public void signout(HttpServletResponse res) {
        jwtTokenManager.signOut(res);
    }

    // 내 정보 조회
    public UserGetRes getUser(Long userNo) {
        User user = userMapper.findByUserNo(userNo);
        return new UserGetRes(user.getUserId(), user.getName(), user.getTel(), user.getGender(), user.getBirth());
    }

    // 내 정보 수정
    public void updateUser(Long userNo, UserUpdateReq req) {
        if (req.getUserPw() != null && !req.getUserPw().isBlank()) {
            req.setUserPw(passwordEncoder.encode(req.getUserPw()));
        }
        req.setUserNo(userNo);
        userMapper.update(req);
    }

    public void postReview(long user, ReviewReq req) {
        try {
            long userId = userMapper.checkReviewWriter(req);
            if (userId == user) {
                userMapper.postReview(req);
            } else {
                throw new RuntimeException("주문한사용자가 아닙니다");
            }
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("이미 리뷰가 등록되었습니다");
        }
    }

    public Map<String, Object> getReviews(GetReviewReq req) {
        int totalCount = userMapper.countReviews(req.getUserNo());
        List<ReviewRes> list = userMapper.getReviews(req);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);
        result.put("totalPages", (int) Math.ceil((double) totalCount / req.getSize()));
        result.put("currentPage", req.getCurrentPage());
        return result;
    }

    public void deleteReview(long userNo, long reviewId) {
        // 본인 리뷰인지 검증 후 삭제
        int result = userMapper.deleteReview(userNo, reviewId);
        if (result == 0) throw new RuntimeException("삭제 권한이 없습니다.");
    }
}
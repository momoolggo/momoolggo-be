package com.green.momoolggo.application.user;

import com.green.momoolggo.application.user.model.User;
import com.green.momoolggo.application.user.model.UserSigninReq;
import com.green.momoolggo.application.user.model.UserSigninRes;
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

    // ── 로그인
    public UserSigninRes signin(UserSigninReq req, HttpServletResponse res) {

        // 1. 아이디로 DB에서 유저 조회
        User user = userMapper.findByUserId(req.getUserId());
        if (user == null) {
            // ⚠️ 아이디 존재 여부를 노출하지 않기 위해 통일된 메시지 사용
            throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        // 2. 비밀번호 검증
        // passwordEncoder.matches(평문, 암호화된값) → 일치하면 true
        if (!passwordEncoder.matches(req.getUserPw(), user.getUserPw())) {
            throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
        }

        // 3. JWT 발급
        // JwtUser : JWT 토큰 안에 담을 유저 정보 (userNo, role)
        // jwtTokenManager.issue() : AT + RT 생성 후 쿠키에 저장
        JwtUser jwtUser = new JwtUser(user.getUserNo(), user.getRole(), null);
        jwtTokenManager.issue(res, jwtUser);

        // 4. 프론트로 반환할 응답 데이터 (userPw 같은 민감한 정보는 포함 안 함)
        return new UserSigninRes(
                user.getUserNo(),
                user.getName(),
                user.getRole()
        );
    }

    // ── 로그아웃
    public void signout(HttpServletResponse res) {
        // 쿠키에서 AT, RT 삭제
        jwtTokenManager.signOut(res);
    }
}
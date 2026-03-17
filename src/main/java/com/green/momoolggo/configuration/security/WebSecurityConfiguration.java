package com.green.momoolggo.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// Spring Security 전체 설정을 담당하는 클래스
// 어떤 요청을 허용/차단할지, 어떤 필터를 거칠지 정의함
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    // 모든 요청마다 쿠키에서 JWT 를 꺼내 인증 처리하는 필터
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 이 줄을 추가하세요!
                // JWT 사용 → 서버에서 세션을 만들지 않음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Spring Security 기본 로그인 화면 비활성화
                .httpBasic(hb -> hb.disable())
                // form 로그인 비활성화 (REST API 방식 사용)
                .formLogin(fl -> fl.disable())
                // CSRF 비활성화 (REST API + JWT 방식에서는 불필요)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(req -> req

                        // ── 인증 없이 허용 (비로그인도 가능)
                        .requestMatchers(HttpMethod.POST, "/api/user/login").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/user/check-id").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/join").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/address/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/map/key").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/user/me").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/address/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/reissue").permitAll()

                        // 가게/메뉴/카테고리/리뷰 조회는 비로그인도 가능
                        .requestMatchers(HttpMethod.GET, "/api/store").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/store/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/menu").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/menu/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/category").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/review/**").permitAll()

                        // 내 정보 조회 (Vue 역할 분기용)
                        .requestMatchers(HttpMethod.GET, "/api/user/me").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/owner/menu").permitAll()

                        // ── OWNER 전용 (사장만 가능)
                        // /api/owner/** 로 시작하는 모든 요청은 OWNER 역할만 접근 가능
                        .requestMatchers("/api/owner/**").permitAll()

                        // ── 나머지 요청은 로그인 필수
                        // (유저 조회/수정, 찜, 장바구니, 주문, 리뷰 작성 등)
                        .anyRequest().authenticated()
                )
                // Spring Security 기본 로그인 필터 앞에 JWT 필터를 끼워 넣음
                // 요청이 들어오면 TokenAuthenticationFilter → 나머지 필터 순서로 실행
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 프론트엔드 주소 허용
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        // 허용할 HTTP 메서드 (POST를 포함해야 합니다)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 모든 헤더 허용
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 자격 증명(쿠키, 인증 헤더) 허용
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 비밀번호 암호화에 사용할 인코더 빈 등록
    // BCrypt : 현존 가장 안전한 단방향 암호화 알고리즘
    // UserService 에서 passwordEncoder.encode() / .matches() 로 사용함
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
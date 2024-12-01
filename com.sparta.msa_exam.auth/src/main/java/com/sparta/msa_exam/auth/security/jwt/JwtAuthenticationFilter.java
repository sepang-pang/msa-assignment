package com.sparta.msa_exam.auth.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa_exam.auth.domain.dto.req.ReqAuthPostSignInDTO;
import com.sparta.msa_exam.auth.domain.dto.res.ResDTO;
import com.sparta.msa_exam.auth.model.constraint.RoleType;
import com.sparta.msa_exam.auth.security.userdetails.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/auth/sign-in");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            ReqAuthPostSignInDTO dto = new ObjectMapper().readValue(request.getInputStream(), ReqAuthPostSignInDTO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUser().getUsername(),
                            dto.getUser().getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = ((CustomUserDetails) authResult.getPrincipal()).getUsername();
        Long userId = ((CustomUserDetails) authResult.getPrincipal()).getUserId();
        RoleType role = ((CustomUserDetails) authResult.getPrincipal()).getUser().getRoleType();
        String token = jwtUtil.createToken(username, userId, role);

        // ResDTO 객체 생성
        ResDTO<String> resDTO = ResDTO.<String>builder()
                .code(HttpStatus.OK.value())
                .message("로그인 성공")
                .data(username)
                .build();

        log.info("로그인 성공 및 JWT 생성");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(resDTO));

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}

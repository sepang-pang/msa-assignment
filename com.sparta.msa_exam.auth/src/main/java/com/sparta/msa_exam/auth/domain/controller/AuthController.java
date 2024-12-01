package com.sparta.msa_exam.auth.domain.controller;

import com.sparta.msa_exam.auth.domain.dto.req.ReqAuthPostSignUpDTO;
import com.sparta.msa_exam.auth.domain.dto.res.ResDTO;
import com.sparta.msa_exam.auth.domain.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResDTO<Object>> signupBy(@Valid @RequestBody ReqAuthPostSignUpDTO dto) {
        return authService.signupBy(dto);
    }
 }

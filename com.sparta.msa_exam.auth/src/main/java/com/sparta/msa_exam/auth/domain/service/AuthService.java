package com.sparta.msa_exam.auth.domain.service;

import com.sparta.msa_exam.auth.domain.dto.req.ReqAuthPostSignUpDTO;
import com.sparta.msa_exam.auth.domain.dto.res.ResDTO;
import com.sparta.msa_exam.auth.model.constraint.RoleType;
import com.sparta.msa_exam.auth.model.entity.UserEntity;
import com.sparta.msa_exam.auth.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<ResDTO<Object>> signupBy(ReqAuthPostSignUpDTO dto) {

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(dto.getUser().getUsername());
        if (userEntityOptional.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        UserEntity userEntityForSaving = UserEntity.builder()
                .username(dto.getUser().getUsername())
                .password(passwordEncoder.encode(dto.getUser().getPassword()))
                .roleType(RoleType.USER)
                .build();

        userRepository.save(userEntityForSaving);

        return new ResponseEntity<>(
                ResDTO.builder()
                        .code(HttpStatus.OK.value())
                        .message("회원가입에 성공하였습니다.")
                        .build(),
                HttpStatus.OK
        );
    }
}

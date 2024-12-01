package com.sparta.msa_exam.auth.model.entity;

import com.sparta.msa_exam.auth.model.constraint.RoleType;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {

    @Id @Tsid
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleType roleType;

    @Builder
    public UserEntity(String username, String password, RoleType roleType) {
        this.username = username;
        this.password = password;
        this.roleType = roleType;
    }
}

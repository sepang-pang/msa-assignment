package com.sparta.msa_exam.auth.model.constraint;

public enum RoleType {
    USER(Authority.USER), // 사용자 권한
    OWNER(Authority.OWNER), // 사장 권한
    ADMIN(Authority.ADMIN); // 관리자 권한

    private final String authority;

    RoleType(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String ADMIN = "ROLD_ADMIN";
    }
}

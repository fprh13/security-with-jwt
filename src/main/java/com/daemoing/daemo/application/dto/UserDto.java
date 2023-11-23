package com.daemoing.daemo.application.dto;

import com.daemoing.daemo.domain.Univ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class UserDto {



    /**
     * CREATE
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class JoinDto {

        // TODO: 아래 예시 Validation 조건 수정 할 것

        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        @NotNull(message = "아이디가 NULL 입니다.")
        @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
        private String loginId;

        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        @NotNull(message = "비밀번호가 NULL 입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$"
                , message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 포함 해야 합니다.")
        private String password;

        @NotBlank(message = "이름이 입력되지 않았습니다.")
        @NotNull(message = "이름이 NULL 입니다.")
        private String username;

        @Size(max = 30, message = "프로필 설명은 30자를 넘을 수 없습니다.")
        private String description;

        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @NotNull(message = "학번이 NULL 입니다.")
        private int studentId;

        @Pattern(regexp = "^(MALE|FEMALE)$", message = "Invalid gender: Must be MALE or FEMALE")
        private String gender;

        @Valid
        private Univ univ;
    }


    /**
     * UPDATE
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class UpdateDto {
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        @NotNull(message = "아이디가 NULL 입니다.")
        @Pattern(regexp = "^[a-z0-9]{4,20}$", message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
        private String loginId;

        @NotBlank(message = "이름이 입력되지 않았습니다.")
        @NotNull(message = "이름이 NULL 입니다.")
        private String username;

        @Size(max = 30, message = "프로필 설명은 30자를 넘을 수 없습니다.")
        private String description;

        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @NotNull(message = "학번이 NULL 입니다.")
        private int studentId;

        @Pattern(regexp = "^(MALE|FEMALE)$", message = "Invalid gender: Must be MALE or FEMALE")
        private String gender;

        @Valid
        private Univ univ;
    }



    /**
     * READ
     * 1. 내 정보
     * 2. 회원 정보 수정 데이터
     * 3. 다른 회원 정보 조회
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class InfoDto {
        private String loginId;
        private String username;
        private String description;
        private String email;
        private int studentId;
        private String gender;
        private Univ univ;
    }


}


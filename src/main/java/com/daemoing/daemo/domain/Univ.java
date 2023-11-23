package com.daemoing.daemo.domain;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Univ {

    /**
     * 대학 이름
     */
    @NotBlank(message = "대학 이름이 입력되지 않았습니다.")
    @NotNull(message = "대학 이름이 NULL 입니다.")
    private String name;

    /**
     * 대학 위치
     */
    @NotBlank(message = "대학 위치가 입력되지 않았습니다.")
    @NotNull(message = "대학 위치가 NULL 입니다.")
    private String city;
}


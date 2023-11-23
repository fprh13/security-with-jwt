package com.daemoing.daemo.domain;

import com.daemoing.daemo.global.auditing.BaseCreateByEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
@Builder
@AllArgsConstructor
@Getter
public class Board extends BaseCreateByEntity {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title; // 글 제목

    private String content; // 글 내용

    //TODO: 사진 업로드 관련 컬럼 추가

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;



    //==업데이트 로직==//
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

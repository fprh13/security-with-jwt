package com.daemoing.daemo.global.auditing;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseCreateByEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false)
    private String createBy;
}


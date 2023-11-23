package com.daemoing.daemo.repository;

import com.daemoing.daemo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * READ - ME
     * 1. 나의 정보 조회
     */
    User findMyInfoByLoginId(String loginId);


    /**
     * READ - NOT ME
     * 1. security
     * 2. 다른 유저 정보 조회
     */
    Optional<User> findByLoginId(String loginId);


    /**
     * DELETE
     * 1. 회원 탈퇴
     */
    void deleteByLoginId(String loginId);

    /**
     * 로그인 중복 체크
     */
    boolean existsByLoginId(String loginId);
}

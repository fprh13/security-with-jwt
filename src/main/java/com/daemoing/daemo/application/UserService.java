package com.daemoing.daemo.application;

import com.daemoing.daemo.domain.type.Gender;
import com.daemoing.daemo.domain.type.Role;
import com.daemoing.daemo.domain.User;
import com.daemoing.daemo.global.common.ErrorCode;
import com.daemoing.daemo.global.common.exception.CustomException;
import com.daemoing.daemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

import static com.daemoing.daemo.application.dto.UserDto.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    /**아이디 중복 확인**/
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    /**
     * CREATE
     */
    @Transactional
    public Long join(JoinDto joinDto) {
        User user = userRepository.save(User.builder()
                .loginId(joinDto.getLoginId())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .username(joinDto.getUsername())
                .description(joinDto.getDescription())
                .email(joinDto.getEmail())
                .studentId(joinDto.getStudentId())
                .gender(Gender.valueOf(joinDto.getGender()))
                .univ(joinDto.getUniv())
                .role(Role.USER)
                .build());
        log.info(joinDto.getLoginId() + " : " + "join" + "(" + new Date() + ")");
        return user.getId();
    }

    /**
     * UPDATE
     */
    @Transactional
    public Long update(UpdateDto updateDto, String principal) {
        User user = userRepository.findByLoginId(principal)
                .orElseThrow(() -> new RuntimeException("회원이 없습니다."));
        user.update(updateDto.getLoginId(),
                updateDto.getUsername(),
                updateDto.getDescription(),
                updateDto.getEmail(),
                updateDto.getStudentId(),
                Gender.valueOf(updateDto.getGender()),
                updateDto.getUniv());
        return user.getId();
    }



    /**
     * READ - ME
     */
    public InfoDto info() {
        User user = userRepository.findMyInfoByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        return InfoDto.builder()
                .loginId(user.getLoginId())
                .username(user.getUsername())
                .description(user.getDescription())
                .email(user.getEmail())
                .studentId(user.getStudentId())
                .gender(user.getGender().toString())
                .univ(user.getUniv())
                .build();
    }

    /**
     * READ - OTHERS
     */
    public InfoDto othersInfo(String loginId) {
        User othersUser = userRepository.findByLoginId(loginId).orElseThrow(
                ()->new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        return InfoDto.builder()
                .loginId(othersUser.getLoginId())
                .username(othersUser.getUsername())
                .description(othersUser.getDescription())
                .email(othersUser.getEmail())
                .studentId(othersUser.getStudentId())
                .gender(othersUser.getGender().toString())
                .univ(othersUser.getUniv())
                .build();
    }
    }


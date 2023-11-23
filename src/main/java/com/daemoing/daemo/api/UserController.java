package com.daemoing.daemo.api;

import com.daemoing.daemo.api.response.ResponseDto;
import com.daemoing.daemo.application.UserService;
import com.daemoing.daemo.global.common.ErrorCode;
import com.daemoing.daemo.global.common.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;

import static com.daemoing.daemo.application.dto.UserDto.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/duplication/{loginId}")
    @Operation(summary = "아이디 중복 검사 요청", description = "**중복 된 아이디라면 `409`에러가 반환됩니다.**", tags = {"User"})
    public ResponseEntity<ResponseDto> duplication(@PathVariable String loginId) {
        if (!userService.checkLoginIdDuplicate(loginId)) {
            return ResponseEntity.ok().body(ResponseDto.success(true));
        } throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
    }



    /**
     * CREATE
     */
    @PostMapping("/join")
    @Operation(summary = "회원가입 요청", description = "**비밀번호 :** `영문 + 숫자 + 특수 문자 / 8 글자 이상`", tags = {"User"})
    public ResponseEntity<ResponseDto> join(@Valid @RequestBody JoinDto joinDto) {
        return ResponseEntity.ok().body(ResponseDto.success(userService.join(joinDto)));
    }

    /**
     * UPDATE
     */
    @PatchMapping("/mypage/update")
    @Operation(summary = "유저 업데이트 요청", description = "**아이디가 변경되면 `RT토큰 삭제` 및 `재로그인`을 요구합니다.**", tags = {"User"})
    public ResponseEntity<ResponseDto> update(@Valid @RequestBody UpdateDto updateDto) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        // 로그인 아이디가 변경되지 않을 경우
        if (updateDto.getLoginId().equals(principal)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseDto.success(userService.update(updateDto, principal)));
        }
        // 로그인 아이디가 변경되는 경우 -> 재 로그인
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        log.info(principal + " -> " + updateDto.getLoginId() + " : " + "update loinId" + "(" + new Date() + ")");

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ResponseDto.success(userService.update(updateDto, principal)));

    }


    /**
     * READ - ME
     */
    @GetMapping("/mypage/info")
    @Operation(summary = "내 정보 요청", description = "**`나의 정보` 응답합니다.**", tags = {"User"})
    public ResponseEntity<ResponseDto> info() {
        return ResponseEntity.ok().body(ResponseDto.success(userService.info()));
    }

    /**
     * READ - OTHERS
     */
    @GetMapping("/{loginId}/info")
    @Operation(summary = "다른 유저 정보 요청", description = "**`다른 유저의 정보`를 요청 합니다.**", tags = {"User"})
    public ResponseEntity<ResponseDto> othersInfo(@PathVariable String loginId) {
        return ResponseEntity.ok().body(ResponseDto.success(userService.othersInfo(loginId)));
    }


}

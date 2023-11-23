package com.daemoing.daemo.api;


import com.daemoing.daemo.api.response.ResponseDto;
import com.daemoing.daemo.application.AuthService;
import com.daemoing.daemo.global.common.ErrorCode;
import com.daemoing.daemo.global.common.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.Date;

import static com.daemoing.daemo.application.dto.AuthDto.*;

/**
 * 로그인,로그아웃,토큰검사,토큰재발급
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final long COOKIE_EXPIRATION = 7776000; // 90일




    /**
     * 로그인 -> 토큰 발급
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 요청", description = "**로그인 성공 시 헤더에 `토큰 정보가 반환`됩니다.**", tags = {"Auth"})
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        // User 등록 및 Refresh Token 저장
        TokenDto tokenDto = authService.login(loginDto);

        // RT 저장
        HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenDto.getRefreshToken())
                .maxAge(COOKIE_EXPIRATION)
                .httpOnly(true)
                .secure(true)
                .build();

        log.info(SecurityContextHolder.getContext().getAuthentication().getName() + " : " + "login"
        + "(" + new Date() + ")");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                // AT 저장
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .body(ResponseDto.success(null));
    }

    /**
     * 로그아웃 -> RT 초기화
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃 요청", description = "**로그아웃 시 `RT 토큰을 초기화하고 재로그인`을 요구합니다.**", tags = {"Auth"})
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        authService.logout(requestAccessToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ResponseDto.success(null));
    }

    /**
     * 회원 탈퇴 -> RT, AT 무효 처리 및 RT 초기화
     */
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴 요청", description = "**회원 탈퇴 시 `RT 토큰을 초기화하고 재로그인`을 요구합니다.**", tags = {"Auth"})
    public ResponseEntity<ResponseDto> delete(@RequestHeader("Authorization") String requestAccessToken) {
        authService.delete(requestAccessToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ResponseDto.success(null));
    }

    /**
     * AT 유효성 검증
     * 토큰 유효기간이 지났다면 401 , 없는 유저면 403 에러
     */
    @PostMapping("/validate")
    @Operation(summary = "토큰 유효성 검사", description = "**`토큰의 유효기간이 지나면 401에러`, `토큰 변조 및 없는 유저라면 403에러`를 반환 합니다.**", tags = {"Auth"})
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(null)); // 재발급 필요X
        } else {
            // 헤더 설정 없이 에러만 반환 하기 때문에 Custom 사용
            throw new CustomException(ErrorCode.EXPIRED_AUTH_TOKEN);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.fail("재발급이 필요합니다.",HttpStatus.UNAUTHORIZED)); // 재발급 필요
        }
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    @Operation(summary = "재발급 요청", description = "**성공시 토큰을 헤더에 넣어 응답합니다. `유효성 검증이 안된 토큰이라면 RT 삭제 및 재로그인`을 요구합니다. -> `401에러`**", tags = {"Auth"})
    public ResponseEntity<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        TokenDto reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        if (reissuedTokenDto != null) { // 토큰 재발급 성공
            // RT 저장
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    // AT 저장
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .body(ResponseDto.success(null));
        } else { // Refresh Token 탈취 가능성
            // Cookie 삭제 후 재로그인 유도
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(ResponseDto.fail("토큰이 재발급 실패, 다시 로그인 하세요.",HttpStatus.UNAUTHORIZED));
        }
    }


}

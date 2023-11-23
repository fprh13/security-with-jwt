package com.daemoing.daemo.global.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Data
@Builder
@Slf4j
public class CustomErrorResponseDto {
    private boolean success;
    private int status;
    private String code;
    private String message;

    /**
     * exception
     */
    public static ResponseEntity<CustomErrorResponseDto> fail(ErrorCode e){
        log.error(e.getHttpStatus().value() +"에러 발생 -> " + e.getMessage());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(CustomErrorResponseDto.builder()
                        .success(false)
                        .status(e.getHttpStatus().value())
                        .code(e.name())
                        .message(e.getMessage())
                        .build()
                );
    }

    /**
     * validation
     */
    public static ResponseEntity<CustomErrorResponseDto> valid(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        FieldError fieldError = fieldErrors.get(fieldErrors.size()-1);  // 가장 첫 번째 에러 필드
        String fieldName = fieldError.getField();   // 필드명
        Object rejectedValue = fieldError.getRejectedValue();   // 입력값

        log.error(400 +"에러 발생 -> " + fieldName + " 필드의 입력값[ " + rejectedValue + " ]이 유효하지 않습니다.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CustomErrorResponseDto.builder()
                        // 에러 코드 in 에러 코드 명세서
                        .success(false)
                        .status(400)
                        .code(fieldError.getDefaultMessage())
                        .message(fieldName + " 필드의 입력값[ " + rejectedValue + " ]이 유효하지 않습니다.")
                        .build());
    }
}

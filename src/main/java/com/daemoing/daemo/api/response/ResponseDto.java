package com.daemoing.daemo.api.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto<T> {

    private boolean success;

    private int status;

    private String code;

    private String message;

    private T response;


    @Builder
    public ResponseDto(boolean success, String code ,String message, int status, T response) {
        this.success = success;
        this.status = status;
        this.code = code;
        this.message = message;
        this.response = response;
    }

    /**
     * 성공 응답
     */
    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .code("OK")
                .message("성공적으로 처리되었습니다.")
                .response(data)
                .build();
    }

    /**
     * 실패 응답
     */
    public static <T> ResponseDto<T> fail(String message,HttpStatus status) {
        return ResponseDto.<T>builder()
                .success(false)
                .code(status.name())
                .message(message)
                .status(status.value())
                .response(null)
                .build();
    }

}


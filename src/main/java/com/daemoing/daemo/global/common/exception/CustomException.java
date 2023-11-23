package com.daemoing.daemo.global.common.exception;

import com.daemoing.daemo.global.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    ErrorCode errorCode;
}

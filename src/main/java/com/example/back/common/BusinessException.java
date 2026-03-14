package com.example.back.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final Integer code;
    private final String message;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
        this.message = message;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }
}

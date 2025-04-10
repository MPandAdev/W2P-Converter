package com.mpanda.word2pdf.exception;

import lombok.Getter;

/**
 * @author :
 * @date :2023/11/29 19:46
 * @description :
 * @modyified By:
 */
public class CommonException extends RuntimeException {

    @Getter
    private String code;
    @Getter
    private String message;

    public CommonException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public CommonException(String message) {
        super(message);
        this.code = "-9";
        this.message = message;
    }
}
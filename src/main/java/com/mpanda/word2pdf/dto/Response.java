package com.mpanda.word2pdf.dto;

import com.mpanda.word2pdf.enums.ResponseTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("返回对象")
@Data
public final class Response<T> {

    @ApiModelProperty("返回code")
    private String code;
    @ApiModelProperty("返回message")
    private String message;
    @ApiModelProperty("返回值")
    private T data;

    private String requestId;

    /**
     * 无参构造方法
     */
    public Response() {
        this.requestId = RequestHolder.getRequestId();
    }

    public Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response build() {
        return build(ResponseTypeEnum.SUCCESS.getResultCode(), ResponseTypeEnum.SUCCESS.getResultDesc(), null);
    }

    public static <T> Response build(T data) {
        return build(ResponseTypeEnum.SUCCESS, data);
    }

    public static <T> Response build(ResponseTypeEnum responseTypeEnum,T data) {
        return build(responseTypeEnum.getResultCode(), ResponseTypeEnum.SUCCESS.getResultDesc(), data);
    }

    public static <T> Response build(ResponseTypeEnum responseTypeEnum,T data, String message) {
        return build(responseTypeEnum.getResultCode(), message, data);
    }

    public static <T> Response build(ResponseTypeEnum responseTypeEnum, String message) {
        return build(responseTypeEnum.getResultCode(), message, null);
    }

    public static <T> Response build(String code, String message, T data) {
        return new Response(code, message, data);
    }

}
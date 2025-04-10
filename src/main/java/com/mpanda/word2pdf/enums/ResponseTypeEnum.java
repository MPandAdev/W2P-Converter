package com.mpanda.word2pdf.enums;

import lombok.Getter;

/**
 * @author :
 * @date :2023/11/29 18:56
 * @description :
 * @modyified By:
 */
@Getter
public enum ResponseTypeEnum {
    SUCCESS("0","success"),
    PARAM_DEFECT("-1","参数缺失"),
    SYSTEM_ERROR("-2","系统错误"),
    PARAM_ERROR("-3","参数错误"),
    ERROR_UPLOAD_FILE("-4","文件上传错误"),
    USER_ERROR("-5","用戶信息错误")
    ;


    private String resultCode;

    private String resultDesc;


    ResponseTypeEnum(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }
}
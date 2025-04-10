package com.mpanda.word2pdf.dto;

import lombok.Data;

/**
 * @author :
 * @date :2023/11/29 19:27
 * @description :
 * @modyified By:
 */
@Data
public class UploadFileInfoBase64Dto extends SSOBaseInfoDto {
    private String fileName;
    private String base64;
}
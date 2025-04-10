package com.mpanda.word2pdf.dto;

import lombok.Data;

/**
 * @author :
 * @date :2023/11/29 19:27
 * @description :
 * @modyified By:
 */
@Data
public class UploadFileInfoRes {
    private String filePath;
    private String fileName;
    private String displayFileName;
    private Long fileSize;
    private String eTag;
}
package com.mpanda.word2pdf.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author :
 * @date :2023/11/29 19:27
 * @description :
 * @modyified By:
 */
@Data
public class SSOBaseInfoDto {
    @ApiModelProperty(value = "sso EndPoint", example = "oss-cn-beijing.aliyuncs.com")
    private String endPoint;
    @ApiModelProperty(value = "sso AccessKeyId", example = "")
    private String accessKeyId;
    @ApiModelProperty(value = "sso AccessKeySecret", example = "")
    private String accessKeySecret;
    @ApiModelProperty(value = "sso BucketName", example = "ssd-dev-bucket")
    private String bucketName;
    @ApiModelProperty(value = "sso Filedir", example = "ecommercial/")
    private String filedir;
}
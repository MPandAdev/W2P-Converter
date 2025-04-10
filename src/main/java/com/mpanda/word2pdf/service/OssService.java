package com.mpanda.word2pdf.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.PutObjectResult;
import com.mpanda.word2pdf.dto.SSOBaseInfoDto;
import com.mpanda.word2pdf.enums.ResponseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author :
 * @date :2023/11/30 10:30
 * @description :
 * @modyified By:
 */
@Service
@Slf4j
public class OssService {
    /**
     * 上传到OSS服务器
     *
     * @param inputStream 文件流
     * @param filePath 文件存储目录
     * @param fileName 文件名称 包括后缀名
     * @return
     */
    public String uploadFile2Oss(SSOBaseInfoDto ossBaseInfo, InputStream inputStream, String filePath, String fileName){
        OSS ossClient = new OSSClientBuilder().build(ossBaseInfo.getEndPoint(),ossBaseInfo.getAccessKeyId(),ossBaseInfo.getAccessKeySecret());
        String ret = "";
        try {
            //上传文件
            PutObjectResult putResult = ossClient.putObject(ossBaseInfo.getBucketName(), filePath + fileName, inputStream);
            ret = putResult.getETag();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                ossClient.shutdown();
            } catch (IOException e) {
                throw new ServiceException(ResponseTypeEnum.ERROR_UPLOAD_FILE.getResultDesc());
            }
        }
        return ret;
    }
}
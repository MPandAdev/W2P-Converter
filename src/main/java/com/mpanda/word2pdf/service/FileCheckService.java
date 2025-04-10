package com.mpanda.word2pdf.service;

import com.mpanda.word2pdf.config.FileCheckConfig;
import com.mpanda.word2pdf.exception.CommonException;
import com.mpanda.word2pdf.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author :
 * @date :2023/11/29 19:40
 * @description :
 * @modyified By:
 */
@Component
public class FileCheckService {

    @Autowired
    private FileCheckConfig fileCheckConfig;

    public FileCheckService(FileCheckConfig fileCheckConfig) {
        this.fileCheckConfig = fileCheckConfig;
    }

    public Boolean isValid(MultipartFile multipartFile) throws CommonException {
        Double maxSize = fileCheckConfig.getMaxSize();
        // 校验上传文件的类型，文件的大小，根据文件的头信息返回文件的真实类型
        return FileUtils.checkFile(multipartFile, maxSize, fileCheckConfig.getTypes());
    }
    public Boolean isValid(String base64,String fileName) throws CommonException {
        Double maxSize = fileCheckConfig.getMaxSize();
        // 校验上传文件的类型，文件的大小，根据文件的头信息返回文件的真实类型
        return FileUtils.checkFile(base64, fileName, maxSize, fileCheckConfig.getTypes());
    }
}
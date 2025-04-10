package com.mpanda.word2pdf.service;

import com.mpanda.word2pdf.utils.FileUtils;
import com.mpanda.word2pdf.utils.WordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author :
 * @date :2023/11/30 11:14
 * @description :
 * @modyified By:
 */
@Slf4j
@Service
public class CoreService {
    public String ConvertAndSave(MultipartFile file) throws IOException {
        String filePath = FileUtils.saveFile(file);
        File localOriginFile = new File(filePath);
        String localOriginUUIDFile= FileUtils.renameFileWithUUID(localOriginFile);
        File renamedLocalOriginFile = new File(localOriginUUIDFile);
        String newFilePath = FileUtils.getNewFilePath(renamedLocalOriginFile,renamedLocalOriginFile.getName(),".pdf");
        WordUtils.wordToPdf(localOriginUUIDFile,newFilePath);
        return newFilePath;
    }
    public String ConvertAndSave(String base64,String fileName) throws IOException {
        String filePath = FileUtils.saveFile(base64,fileName);
        File localOriginFile = new File(filePath);
        String localOriginUUIDFile= FileUtils.renameFileWithUUID(localOriginFile);
        File renamedLocalOriginFile = new File(localOriginUUIDFile);
        String newFilePath = FileUtils.getNewFilePath(renamedLocalOriginFile,renamedLocalOriginFile.getName(),".pdf");
        WordUtils.wordToPdf(localOriginUUIDFile,newFilePath);
        return newFilePath;
    }
}
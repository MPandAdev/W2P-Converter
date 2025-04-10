package com.mpanda.word2pdf.controller;

import com.mpanda.word2pdf.dto.UploadFileInfoBase64Dto;
import com.mpanda.word2pdf.dto.UploadFileInfoDto;
import com.mpanda.word2pdf.dto.UploadFileInfoRes;
import com.mpanda.word2pdf.enums.ResponseTypeEnum;
import com.mpanda.word2pdf.exception.CommonException;
import com.mpanda.word2pdf.service.CoreService;
import com.mpanda.word2pdf.service.FileCheckService;
import com.mpanda.word2pdf.dto.Response;
import com.mpanda.word2pdf.service.OssService;
import com.mpanda.word2pdf.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author :
 * @date :2023/11/29 18:52
 * @description :
 * @modyified By:
 */
@RequestMapping("/word2pdf")
@RestController
@CrossOrigin
@Api("word转pdf")
@Slf4j
public class WordUtilsController {
    @Autowired
    private FileCheckService fileCheckService;
    @Autowired
    private OssService ossService;
    @Autowired
    private CoreService coreService;
    @ApiOperation("word转pdf")
    @PostMapping("upload")
    public Response word2pdf(@RequestPart("file")MultipartFile file, @RequestPart("target") UploadFileInfoDto uploadFileInfoDto, HttpServletRequest request){
        UploadFileInfoRes req = new UploadFileInfoRes();
        String newFilePath = "";
        try{
            log.info("{} 服务发起请求:将\"{}\"转换为PDF并上传至\"{}\"的\"{}\"目录",request.getRemoteAddr(),file.getName(),uploadFileInfoDto.getBucketName(),uploadFileInfoDto.getFiledir());
            if(fileCheckService.isValid(file)){
                // 转换并保存
                newFilePath = coreService.ConvertAndSave(file);

                File convertedFile = new File(newFilePath);
                FileInputStream fileInputStream = new FileInputStream(convertedFile);
                String filePathOnOSS = uploadFileInfoDto.getFiledir();

                String etag = ossService.uploadFile2Oss(uploadFileInfoDto,fileInputStream,filePathOnOSS,convertedFile.getName());

                req.setFileName(convertedFile.getName());
                req.setDisplayFileName(FileUtils.getNewFileName(file,".pdf"));
                req.setFilePath(uploadFileInfoDto.getFiledir()+convertedFile.getName());
                req.setFileSize(convertedFile.length());
                req.setETag(etag);
            }
        } catch (CommonException e) {
            return Response.build(e.getCode(), e.getMessage(),null);
        } catch (Exception e) {
            return Response.build(ResponseTypeEnum.SYSTEM_ERROR,null,e.getMessage());
        } finally {
            if(StringUtils.isNotEmpty(newFilePath)){
                File convertedFile = new File(newFilePath);
                if(convertedFile.exists()){
                    log.info("删除本地转换后的文件:{}",convertedFile.getName());
                    convertedFile.delete();
                }
            }
        }
        return Response.build(req);
    }

    @ApiOperation("word转pdf base64")
    @PostMapping("base64")
    public Response word2pdfInBase4(@RequestBody UploadFileInfoBase64Dto uploadFileInfoDto, HttpServletRequest request){
        UploadFileInfoRes req = new UploadFileInfoRes();
        String newFilePath = "";
        try{
            log.info("{} 服务发起请求:将\"{}\"转换为PDF并上传至\"{}\"的\"{}\"目录",request.getRemoteAddr(),uploadFileInfoDto.getFileName(),uploadFileInfoDto.getBucketName(),uploadFileInfoDto.getFiledir());
            if(fileCheckService.isValid(uploadFileInfoDto.getBase64(),uploadFileInfoDto.getFileName())){
                // 转换并保存
                newFilePath = coreService.ConvertAndSave(uploadFileInfoDto.getBase64(),uploadFileInfoDto.getFileName());

                File convertedFile = new File(newFilePath);
                FileInputStream fileInputStream = new FileInputStream(convertedFile);
                String filePathOnOSS = uploadFileInfoDto.getFiledir();

                String etag = ossService.uploadFile2Oss(uploadFileInfoDto,fileInputStream,filePathOnOSS,convertedFile.getName());

                req.setFileName(convertedFile.getName());
                req.setDisplayFileName(FileUtils.getNewFileName(uploadFileInfoDto.getFileName(),".pdf"));
                req.setFilePath(uploadFileInfoDto.getFiledir()+convertedFile.getName());
                req.setFileSize(convertedFile.length());
                req.setETag(etag);
            }
        } catch (CommonException e) {
            return Response.build(e.getCode(), e.getMessage(),null);
        } catch (Exception e) {
            return Response.build(ResponseTypeEnum.SYSTEM_ERROR,null,e.getMessage());
        } finally {
            if(StringUtils.isNotEmpty(newFilePath)){
                File convertedFile = new File(newFilePath);
                if(convertedFile.exists()){
                    log.info("删除本地转换后的文件:{}",convertedFile.getName());
                    convertedFile.delete();
                }
            }
        }
        return Response.build(req);
    }

    @ApiOperation("服务检测")
    @GetMapping("check")
    public Response heartbeat(){
        return Response.build("OK");
    }
}
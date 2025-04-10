package com.mpanda.word2pdf.utils;

import com.mpanda.word2pdf.enums.BizCodeEnum;
import com.mpanda.word2pdf.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author :
 * @date :2023/11/29 19:41
 * @description :
 * @modyified By:
 */
@Slf4j
public class FileUtils {
    public static final String TEMP_FOLDER_NAME="temp";
    /**
     * 文件类型和文件大小校验
     *
     * @param file            上传的附件
     * @param fileMaxSize     限制上传附件的大小
     * @param allowedFileType 限制上传附件的类型
     */
    public static Boolean checkFile(MultipartFile file, Double fileMaxSize, Map<String, String> allowedFileType) throws CommonException {
        Boolean result = false;
        String fileType;
        // 文件类型判断 - 校验文件后缀
        String fileName = file.getOriginalFilename();
        if (StringUtils.isNotBlank(fileName)) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!fileTypeAllowed(suffix, allowedFileType.keySet())) {
                throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_NOT_ALLOWED);
            }
        } else {
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_FILENAME_NOT_ALLOWED);
        }

        // 文件类型判断 - 校验文件头内容
        try (InputStream inputStream = file.getInputStream()) {
            // 获取到上传文件的文件头信息
            String fileHeader = FileUtils.getFileHeader(inputStream);
            if (StringUtils.isBlank(fileHeader)) {
                log.error("Failed to get file header content.");
                throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_NOT_ALLOWED);
            }
            // 根据上传文件的文件头获取文件的真实类型
            fileType = getFileType(fileHeader, allowedFileType);
            result = fileTypeAllowed(fileType, allowedFileType.keySet());
            if (StringUtils.isBlank(fileType) || !result) {
                log.error("Unsupported file type: [{}]", fileType);
                throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_NOT_ALLOWED);
            }
        } catch (IOException e) {
            log.error("Get file input stream failed.", e);
            throw new CommonException(BizCodeEnum.ATTACHMENT_UPLOAD_ERROR);
        }

        // 文件大小校验 - 单位：MB
        long fileBytes = file.getSize();
        double fileSize = (double) fileBytes / 1048576;
        if (fileSize <= 0) {
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_EMPTY_FILE);
        } else if (fileMaxSize > 0 && fileSize > fileMaxSize) {
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_EXCEED_LIMIT);
        }
        return result;
    }

    public static Boolean checkFile(String base64,String fileName, Double fileMaxSize, Map<String, String> allowedFileType) throws CommonException {
        Boolean result = false;
        String fileType;

        byte[] bytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(bytes);
        int size = fileInputStream.available();

        // 文件类型判断 - 校验文件后缀
        if (StringUtils.isNotBlank(fileName)) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!fileTypeAllowed(suffix, allowedFileType.keySet())) {
                throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_NOT_ALLOWED);
            }
        } else {
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_FILENAME_NOT_ALLOWED);
        }

        // 文件类型判断 - 校验文件头内容
        try (InputStream inputStream = fileInputStream) {
            // 获取到上传文件的文件头信息
            String fileHeader = FileUtils.getFileHeader(inputStream);
            if (StringUtils.isBlank(fileHeader)) {
                log.error("Failed to get file header content.");
                throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_NOT_ALLOWED);
            }
            // 根据上传文件的文件头获取文件的真实类型
            fileType = getFileType(fileHeader, allowedFileType);
            result = fileTypeAllowed(fileType, allowedFileType.keySet());
            if (StringUtils.isBlank(fileType) || !result) {
                log.error("Unsupported file type: [{}]", fileType);
                throw new CommonException(BizCodeEnum.FILE_UPLOAD_TYPE_NOT_ALLOWED);
            }
        } catch (IOException e) {
            log.error("Get file input stream failed.", e);
            throw new CommonException(BizCodeEnum.ATTACHMENT_UPLOAD_ERROR);
        }

        // 文件大小校验 - 单位：MB
        long fileBytes = size;
        double fileSize = (double) fileBytes / 1048576;
        if (fileSize <= 0) {
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_EMPTY_FILE);
        } else if (fileMaxSize > 0 && fileSize > fileMaxSize) {
            throw new CommonException(BizCodeEnum.FILE_UPLOAD_EXCEED_LIMIT);
        }
        return result;
    }

    /**
     * 文件类型校验
     *
     * @param fileType    待校验的类型
     * @param allowedType 允许上传的文件类型
     * @return true - 满足，false - 不满足
     */
    private static boolean fileTypeAllowed(String fileType, Set<String> allowedType) {
        if (StringUtils.isBlank(fileType) || CollectionUtils.isEmpty(allowedType)) {
            return false;
        }
        return allowedType.contains(fileType);
    }

    /**
     * 据文件的头信息获取文件类型
     *
     * @param fileHeader 文件头信息
     * @return 文件类型
     */
    public static String getFileType(String fileHeader, Map<String, String> allowedFileType) {
        if (fileHeader == null || fileHeader.length() == 0) {
            return null;
        }
        fileHeader = fileHeader.toUpperCase();
        Set<String> types = allowedFileType.keySet();
        for (String type : types) {
            boolean b = fileHeader.startsWith(allowedFileType.get(type));
            if (b) {
                return type;
            }
        }
        return null;
    }

    /**
     * 文件头字节数组转为十六进制编码
     *
     * @param content 文件头字节数据
     * @return 十六进制编码
     */
    private static String bytesToHexString(byte[] content) {
        StringBuilder builder = new StringBuilder();
        if (content == null || content.length <= 0) {
            return null;
        }
        String temp;
        for (byte b : content) {
            temp = Integer.toHexString(b & 0xFF).toUpperCase();
            if (temp.length() < 2) {
                builder.append(0);
            }
            builder.append(temp);
        }
        return builder.toString();
    }

    /**
     * 获取文件的文件头信息
     *
     * @param inputStream 输入流
     * @return 文件头信息
     * @throws IOException 异常
     */
    private static String getFileHeader(InputStream inputStream) throws IOException {
        byte[] content = new byte[28];
        inputStream.read(content, 0, content.length);
        return bytesToHexString(content);
    }

    /**
     * 保存到临时目录
     *
     * @param file
     * @throws IOException
     */
    public static String saveFile(MultipartFile file) throws IOException {
        // 获取项目根目录
        String tempDir = getTempFolderDir();

        // 获取保存文件的路径
        String filePath = tempDir + File.separator + file.getOriginalFilename();

        // 保存文件
        File dest = new File(filePath);
        file.transferTo(dest);
        return filePath;
    }

    public static String saveFile(String base64, String fileName) {
        byte[] bytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        FileOutputStream outputStream = null;
        String filePath = null;
        String tempDir = getTempFolderDir();
        try {

            // 获取保存文件的路径
            filePath = tempDir + File.separator + fileName;

            // 保存文件
            File dest = new File(filePath);
            outputStream = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (Exception e) {

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {

                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    public static String getTempFolderDir() {
        // 获取项目根目录
        String rootPath = System.getProperty("user.dir");
        // 定义保存文件的目录
        String tempDir = rootPath + File.separator + TEMP_FOLDER_NAME;
        // 如果目录不存在，则创建目录
        Path path = Paths.get(tempDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new CommonException("创建临时目录失败");
            }
        }
        return tempDir;
    }

    /**
     * 在文件名上添加UUID
     *
     * @param oldFile
     */
    public static String renameFileWithUUID(File oldFile) {
        String oldFileName = oldFile.getName(); // 原文件名
        int index = oldFileName.lastIndexOf(".");
        String suffix = oldFileName.substring(index); // 后缀名

        String substring = oldFile.getName().substring(oldFile.getName().lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        // 新文件名
        String newFilePath = oldFile.getAbsolutePath().replace(oldFileName, name); // 新文件路径
        File newFile = new File(newFilePath);
        if (oldFile.exists()) {
            oldFile.renameTo(newFile);
        }
        return newFilePath;
    }

    /**
     * 根据传入文件，生成新文件名
     *
     * @param oldFile
     * @param newFileName
     * @return
     */
    public static String getNewFilePath(File oldFile, String newFileName, String suffix) {
        String oldFileName = oldFile.getName(); // 原文件名
        int index = oldFileName.lastIndexOf(".");
        String _suffix = null;
        if (StringUtils.isEmpty(suffix)) {
            // 不修改后缀
            _suffix = oldFileName.substring(index); // 后缀名
        } else {
            _suffix = suffix.substring(suffix.lastIndexOf("."));
        }
        newFileName = newFileName.substring(0, newFileName.lastIndexOf("."));
        return oldFile.getAbsolutePath().replace(oldFileName, newFileName + _suffix); // 新文件路径
    }

    public static String getNewFileName(MultipartFile file, String suffix) {
        return getNewFileName(file.getOriginalFilename(), suffix);
    }
    public static String getNewFileName(String fileName, String suffix) {
        String oldFileName = fileName; // 原文件名
        int index = oldFileName.lastIndexOf(".");
        String _suffix = null;
        if (StringUtils.isEmpty(suffix)) {
            // 不修改后缀
            _suffix = oldFileName.substring(index); // 后缀名
        } else {
            _suffix = suffix.substring(suffix.lastIndexOf("."));
        }
        String newFileName = oldFileName.substring(0, oldFileName.lastIndexOf("."));
        return newFileName + _suffix;
    }
}
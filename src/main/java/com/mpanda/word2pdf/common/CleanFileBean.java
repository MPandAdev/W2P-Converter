package com.mpanda.word2pdf.common;

import com.mpanda.word2pdf.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

/**
 * @author :
 * @date :2023/11/30 15:19
 * @description :
 * @modyified By:
 */
@Slf4j
@Component
public class CleanFileBean {
    @PostConstruct
    public void init() {
        this.cleanFiles();
        log.info("Check and clean temp files");
    }

    @PreDestroy
    public void destroy() {
        this.cleanFiles();
        log.info("Check and clean temp files");
    }

    private boolean cleanFiles() {
        String tempFoldDir =  FileUtils.getTempFolderDir();
        File directory = new File(tempFoldDir);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
        return true;
    }
}
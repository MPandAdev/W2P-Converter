package com.mpanda.word2pdf.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author :
 * @date :2023/11/29 22:22
 * @description :
 * @modyified By:
 */
@Slf4j
@Component
public class LoadLibraryUtil {
    static {
        if(DLLFromJARClassLoader.loadLibrary()){
            log.info("[SYSTEM INIT] Load Jacob DLL success!");
        }
    }
}
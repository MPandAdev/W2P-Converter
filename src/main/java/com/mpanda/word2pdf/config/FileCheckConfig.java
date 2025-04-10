package com.mpanda.word2pdf.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author :
 * @date :2023/11/29 19:35
 * @description :
 * @modyified By:
 */

@Data
@Component
@ConfigurationProperties(prefix = "file.check")
public class FileCheckConfig {
    private Double maxSize;
    private Map<String, String> types;
}
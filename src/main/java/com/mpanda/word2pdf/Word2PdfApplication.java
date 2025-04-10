package com.mpanda.word2pdf;

import com.mpanda.word2pdf.common.LoadLibraryUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableConfigurationProperties
@EnableOpenApi
@Import(LoadLibraryUtil.class)
public class Word2PdfApplication {

    public static void main(String[] args) {
        SpringApplication.run(Word2PdfApplication.class, args);
    }

}
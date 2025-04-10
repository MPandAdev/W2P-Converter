package com.mpanda.word2pdf.utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author :
 * @date :2023/11/29 18:32
 * @description :
 * @modyified By:
 */
public class WordUtils {

    private static final Logger logger = LoggerFactory.getLogger(WordUtils.class);
    static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。
    static final int wdFormatPDF = 17;// word转PDF 格式


    /**
     * @param source word路径
     * @param target 生成的pdf路径
     * @return
     */
    public static boolean wordToPdf(String source, String target) {
        logger.info("Word转PDF开始启动...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        boolean success = true;
        try {
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();
            Dispatch doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();
            logger.info("转换文档到PDF：" + target);
            File tofile = new File(target);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(doc, "SaveAs", target, wdFormatPDF);
            Dispatch.call(doc, "Close", false);
            long end = System.currentTimeMillis();
            logger.info("转换完成，用时：" + (end - start) + "ms");
            success = true;
        } catch (Exception e) {
            logger.info("Word转PDF出错：" + e.getMessage());
            success = false;
        } finally {
            if (app != null) {
                app.invoke("Quit", wdDoNotSaveChanges);
            }
            File originFile = new File(source);
            if (originFile.exists()) {
                logger.info("删除源文件:{}", originFile.getName());
                originFile.delete();
            }
        }
        return success;
    }

}
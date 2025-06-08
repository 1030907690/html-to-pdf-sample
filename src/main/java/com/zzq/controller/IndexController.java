package com.zzq.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.velocity.VelocityEngine;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: zzq
 * @date: 4/29/2025 9:50 PM
 */
@RestController
@RequestMapping("/api/index")
public class IndexController {
    private final Logger log = LoggerFactory.getLogger(IndexController.class);

    //    private final TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("vm", TemplateConfig.ResourceMode.CLASSPATH));
    private final TemplateEngine engine = new VelocityEngine(new TemplateConfig("vm", TemplateConfig.ResourceMode.CLASSPATH));

    @GetMapping("/pdf")
    public String pdf(HttpServletResponse response) {
        Template template = engine.getTemplate("index.vm");
        String result = template.render(Dict.create().set("name", "openhtmltopdf"));
        /*OutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/
        FileOutputStream fileOutputStream = null;
        try {
            String filePath = FileUtil.getTmpDirPath() + System.currentTimeMillis() + ".pdf";
            log.info("文件 {} ", filePath);
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        PdfRendererBuilder builder = new PdfRendererBuilder();

        try {
            /*    try {
          builder.useFont(ResourceUtils.getFile("classpath:pdf/fonts/simsun.ttf"), "simsun");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }*/
//            builder.useFont(new File("D:\\Download\\AlibabaPuHuiTi-3-55-Regular\\AlibabaPuHuiTi-3-55-Regular.ttf"), "AlibabaPuHuiTi", 1, BaseRendererBuilder.FontStyle.NORMAL, true);
            builder.useFont(new File("D:\\Download\\AlibabaPuHuiTi-3-55-Regular\\AlibabaPuHuiTi-3-55-Regular.ttf"), "AlibabaPuHuiTi");
            builder.useFastMode();
//            builder.withHtmlContent(result, ResourceUtils.getURL("classpath:pdf/img/").toString());
            builder.withHtmlContent(result, null);
            builder.toStream(fileOutputStream);
            builder.run();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }
}

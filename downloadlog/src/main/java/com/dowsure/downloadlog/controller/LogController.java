package com.dowsure.downloadlog.controller;

import com.dowsure.downloadlog.common.ApiRestResponse;
import com.dowsure.downloadlog.filter.WebLogAspect;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class LogController {

    private final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @GetMapping("/downloadlog/getlog")
    public ApiRestResponse getLog(@RequestParam String p, HttpServletResponse response) {

        //判断后缀是否为log
        String compare = p.substring(p.length() - 4);
        System.out.println(compare);
        if(!compare.equals(".log")){
            log.info("fail : " + "下载"+ p +"失败");
            return ApiRestResponse.error(10013,"参数错误");
        }


        //获得文件夹路径,
        int index = p.lastIndexOf(File.separator);
        String srcDir = p.substring(0, index);
//        System.out.println("srcDir:" + srcDir);
        //获得文件后缀前名字
        String fileName = p.substring(index + 1);
        System.out.println("fileName:" + fileName);


        File srcfile = new File(p);
        if (srcfile.exists()) {
            //设置response的header
            response.setContentType("application/zip");
//            long timeNew = System.currentTimeMillis() / 1000; // 10位数的时间戳
            Date date = new Date();
            //生成时间戳
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
            String timeNew = simpleDateFormat.format(date);
            System.out.println(timeNew);
            response.setHeader("Content-Disposition", "attachment;filename=" + timeNew + fileName + ".zip");

            // 生成的压缩文件
            File file = new File(srcDir + ".zip");
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(file);
            } catch (ZipException e) {
                e.printStackTrace();
            }
            ZipParameters parameters = new ZipParameters();
            // 压缩方式
            parameters.setCompressionMethod(Zip4jConstants.COMP_STORE);
            // 压缩级别
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
            //遍历判断
                try {
                    zipFile.addFile(srcfile, parameters);
                } catch (ZipException e) {
                    e.printStackTrace();
                }

            InputStream fis = null;
            try {
                fis = new FileInputStream(zipFile.getFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                IOUtils.copy(fis, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                file.delete();
            }
            log.info("success : " + "下载"+ p +"成功");
            return ApiRestResponse.success();

        }
        log.info("fail : " + "下载"+ p +"失败");
        return ApiRestResponse.error(10013,"参数错误");
    }
}



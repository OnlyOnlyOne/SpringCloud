package com.hwy.downloadlog.controller;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LogController {

    @GetMapping("/downloadlog/getlog")
    public ResponseEntity<Void> getLog(@RequestParam String p,HttpServletResponse response) {
        //获得文件夹路径
        int index = p.lastIndexOf("/");
        String srcDir = p.substring(0, index);
        System.out.println("srcDir:" + srcDir);
        //获得文件后缀前名字
        int index2 = p.lastIndexOf(".");
        String fileName = p.substring(index + 1, index2);
        System.out.println("fileName:" + fileName);

        // 要打包的文件夹
        File currentFile = new File(srcDir);
        File[] fs = currentFile.listFiles();
        //存在的文件结果集
        List<File> resultList = new ArrayList<>();

        // 遍历文件夹下所有的文件、文件夹
        for (File f : fs) {
            if (f.isFile() && f.getName().equals(p.substring(index + 1))) {
                resultList.add(f);
            }
        }

        if (!resultList.isEmpty()) {
            //设置response的header
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".zip");

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
            for (File f : resultList) {
                try {
                    zipFile.addFile(f, parameters);
                } catch (ZipException e) {
                    e.printStackTrace();
                }
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
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}



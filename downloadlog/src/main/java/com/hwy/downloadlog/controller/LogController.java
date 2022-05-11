package com.hwy.downloadlog.controller;

import ch.qos.logback.core.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.java2d.pipe.BufferedTextPipe;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.FileSystem;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class LogController {
    private static final String zipFilePath = "src/main/resources/zip"; //压缩后存放路径
    private static final String zipName = "logZip"; //文件夹名称

    @GetMapping("/downloadlog/getlog")
    public ResponseEntity<byte[]> getLog(@RequestParam String p, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("p:" + p);
        //获得文件名字
        int index = p.lastIndexOf("/");
        String fileName = p.substring(index);
        //获取项目部署在服务器的路径
//        String path = request.getServletContext().getRealPath("/");
//        String downPath = path + p;
//        System.out.println("downPath:"+downPath);
        File file = new File(p);
        if (file.exists()) {
            fileToZip(response, p);
            //转换为zip文件
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentDispositionFormData("attchment", fileName);
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), httpHeaders, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();

    }

    public String fileToZip(HttpServletResponse response, String p) {
        int index = p.lastIndexOf("/");
        String fileName = p.substring(index);
        //判断文件夹是否存在，不存在则创建
        File file = new File(zipFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        boolean flag = false;
        File sourceFile = new File(p);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件：" + p + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + zipName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + zipName + ".zip" + "打包文件.");
                } else {
                    File sourceFiles = new File(p);

                    fos = new FileOutputStream(zipFile);
                    zos = new ZipOutputStream(new BufferedOutputStream(fos));
                    byte[] bufs = new byte[1024 * 10];

                    //创建ZIP实体，并添加进压缩包
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zos.putNextEntry(zipEntry);
                    //读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(sourceFiles);
                    bis = new BufferedInputStream(fis, 1024 * 10);
                    int read = 0;
                    while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                        zos.write(bufs, 0, read);
                    }
                    bis.close();
                    fis.close();

                    flag = true;

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //关闭流
                try {
                    if (null != bis) bis.close();
                    if (null != zos) zos.close();
                    if (null != fis) fis.close();
                    if (null != fos) fos.close();
                    //打包好压缩包后删除image的图片
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return fileName;
    }

    public static boolean deleteDir(String path) {
        File file = new File(path);
        if (!file.exists()) {//判断是否待删除目录是否存在
            System.err.println("文件夹不存在!");
            return false;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for (String name : content) {
            File temp = new File(path, name);
            System.gc();	//加上确保文件能删除，不然可能删不掉
            if (temp.isDirectory()) {//判断是否是目录
                deleteDir(temp.getAbsolutePath());//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            } else {
                if (!temp.delete()) {//直接删除文件
                    System.err.println("删除失败 ：文件名 " + name);
                }
            }
        }
        return true;
    }

}

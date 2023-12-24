package com.random.carryyou.service.impl;

import com.random.carryyou.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ImageServiceImplTest {

    @Autowired
    private ImageService imageService;

    @Test
    public void test1() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        log.info("year: {}", year);
        log.info("month: {}", month);
    }

    @Test
    public void test2() {
        File file = new File("/Users/lozhu/Downloads/apple-2788616_1280.jpg");
        String originalFilename = file.getName();
        int index = originalFilename.lastIndexOf(".");
        String fileName = originalFilename.substring(0, index);
        String fileExtension = originalFilename.substring(index);
        log.info("fileName: {}", fileName);
        log.info("fileExtension: {}", fileExtension);
    }

    @Test
    public void test4() {
        String imageName = "/User/save/1.2.3.jpg";
        String detectImageSuffix = "_dec";
        int index = imageName.lastIndexOf(".");
        String fileName = imageName.substring(0, index);
        String fileExtension = imageName.substring(index);
        String result = fileName + detectImageSuffix + fileExtension;
        log.info("result: {}", result);
    }

    @Test
    public void test3() {
        String path1 = imageService.generateSavePath("品种检测");
        log.info("path1: {}", path1);
        String path2 = imageService.generateSavePath("成熟度检测");
        log.info("path2: {}", path2);
        String path3 = imageService.generateSavePath("ABCD");
        log.info("path3: {}", path3);
    }
}

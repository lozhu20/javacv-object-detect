package com.random.carryyou.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ImageDetectServiceTest {

    @Autowired
    private ImageDetectService imageDetectService;

    @Test
    public void detectImage() {
//        imageDetectService.detectImage("/Users/lozhu/Documents/projects/carryyou/carryyou-backend/videos/x1.mp4");
    }

    @Test
    public void transFileNameTest() {
        String fileName = "/Users/lozhu/Documents/projects/carryyou/carryyou-backend/videos/x1.mp4";
        String newFileName = imageDetectService.transDetectFileName(fileName);
        log.info("fileName: {}", newFileName);
    }
}
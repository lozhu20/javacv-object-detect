package com.random.carryyou.service;

public interface ImageDetectService {

    void detectImage(String filePath);

    void detectVideo(String filePath);

    String transDetectFileName(String fileName);

}

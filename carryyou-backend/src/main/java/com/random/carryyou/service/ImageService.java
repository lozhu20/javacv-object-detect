package com.random.carryyou.service;

import com.github.pagehelper.PageInfo;
import com.random.carryyou.dto.Image;
import com.random.carryyou.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    Response<Void> uploadImage(MultipartFile multipartFile, String useAs);

    String generateSavePath(String useAs);

    Response<PageInfo<Image>> queryImageList(String imageName, int pageNum, int pageSize);

    void downloadImage(String id, String type) throws NoSuchFieldException, IOException;

    Response<Void> deleteImage(String id);

}

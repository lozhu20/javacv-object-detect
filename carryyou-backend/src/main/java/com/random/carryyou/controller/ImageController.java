package com.random.carryyou.controller;

import com.github.pagehelper.PageInfo;
import com.random.carryyou.dto.Image;
import com.random.carryyou.dto.Response;
import com.random.carryyou.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api")
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/image/upload")
    public Response<Void> uploadImage(MultipartFile file, String useAs) {
        return imageService.uploadImage(file, useAs);
    }

    @GetMapping("/image/list")
    public Response<PageInfo<Image>> queryImageList(@RequestParam int pageNum, @RequestParam int pageSize) {
        return imageService.queryImageList(null, pageNum, pageSize);
    }

    @GetMapping("/image")
    public Response<PageInfo<Image>> queryImage(@RequestParam String imageName,
                                                @RequestParam int pageNum,
                                                @RequestParam int pageSize) {
        return imageService.queryImageList(imageName, pageNum, pageSize);
    }

    @GetMapping("/image/download/{id}/{type}")
    public void downloadImage(@PathVariable String id,
                              @PathVariable String type) throws IOException, NoSuchFieldException {
        imageService.downloadImage(id, type);
    }

    @DeleteMapping("/image/delete/{id}")
    public Response<Void> delImage(@PathVariable String id) {
        return imageService.deleteImage(id);
    }
}

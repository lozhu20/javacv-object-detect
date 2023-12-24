package com.random.carryyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.random.carryyou.constant.RoleEnum;
import com.random.carryyou.constant.UseAsEnum;
import com.random.carryyou.dao.ImageMapper;
import com.random.carryyou.dto.Image;
import com.random.carryyou.dto.Response;
import com.random.carryyou.service.ImageDetectService;
import com.random.carryyou.service.ImageService;
import com.random.carryyou.utils.RequestUtil;
import com.random.carryyou.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Value("${image.base.path}")
    private String imageBasePath;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private ImageDetectService imageDetectService;

    @Override
    public Response<Void> uploadImage(MultipartFile multipartFile, String useAs) {

        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        int index = originalFilename.lastIndexOf(".");
        String fileExtension = originalFilename.substring(index);

        String id = UUID.randomUUID().toString();
        String filePath = this.generateSavePath(useAs) + File.separator + id + fileExtension;
        try {
            multipartFile.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return ResponseUtil.fail("文件上传失败");
        }

        String currentUserId = RequestUtil.getCurrentUserId();

        Image image = new Image();
        image.setId(id);
        image.setImageName(originalFilename);
        image.setSize((int) multipartFile.getSize());
        image.setSavePath(filePath);
        image.setUseAs(useAs);
        image.setCreatedTime(new Date());
        image.setCreatedBy(currentUserId);
        image.setUpdatedTime(new Date());
        image.setUpdatedBy(currentUserId);
        imageMapper.insert(image);

        // 检测图片
        imageDetectService.detectImage(filePath);

        return ResponseUtil.success(null);
    }

    @Override
    public String generateSavePath(String useAs) {

        String subPath;
        if (UseAsEnum.PATH1.getCode().equals(useAs)) {
            subPath = UseAsEnum.PATH1.getCode();
        } else if (UseAsEnum.PATH2.getCode().equals(useAs)) {
            subPath = UseAsEnum.PATH2.getCode();
        } else {
            subPath = UseAsEnum.OTHER.getCode();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String dirPath = imageBasePath + year + File.separator + month + File.separator + subPath;
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return dirPath;
    }

    @Override
    public Response<PageInfo<Image>> queryImageList(String imageName, int pageNum, int pageSize) {
        String currentUserId = null;
        List<String> currentRoleList = RequestUtil.getCurrentRoleList();
        if (currentRoleList.contains(RoleEnum.ROLE_ADMIN.getCode())) {
            currentUserId = RequestUtil.getCurrentUserId();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Image> imageList = imageMapper.selectByNameAndCreatedBy(imageName, currentUserId);
        File file;
        for (Image image : imageList) {
            String savePath = image.getSavePath();
            file = new File(imageDetectService.transDetectImage(savePath));
            if (file.exists()) {
                image.setDetectSuccess("Y");
            } else {
                image.setDetectSuccess("N");
            }
        }
        return ResponseUtil.success(new PageInfo<>(imageList));
    }

    @Override
    public void downloadImage(String id, String type) throws NoSuchFieldException, IOException {
        Image image = imageMapper.selectByPrimaryKey(id);
        String fileName;
        String path;
        // 1=下载原图，2=下载检测后图片
        if ("1".equals(type)) {
            fileName = image.getImageName();
            path = image.getSavePath();
        } else {
            fileName = imageDetectService.transDetectImage(image.getImageName());
            path = imageDetectService.transDetectImage(image.getSavePath());
        }

        File file = new File(path);

        if (!file.exists()) {
            log.error("文件不存在, {}", path);
            throw new NoSuchFieldException("该文件不存在");
        }

        HttpServletResponse response = RequestUtil.getResponse();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

        InputStream inputStream = Files.newInputStream(file.toPath());
        OutputStream outputStream = response.getOutputStream();
        byte[] buff = new byte[1024];
        int index;
        while ((index = inputStream.read(buff)) != -1) {
            outputStream.write(buff, 0, index);
            outputStream.flush();
        }

        inputStream.close();
        outputStream.close();
    }

    @Override
    public Response<Void> deleteImage(String id) {
        Image image = imageMapper.selectByPrimaryKey(id);
        File file = new File(image.getSavePath());
        if (file.exists()) {
            try {
                file.delete();
            } catch (Exception e) {
                log.error("图片删除失败, id: {}", id);
            }
        }

        File decFile = new File(imageDetectService.transDetectImage(image.getSavePath()));
        if (decFile.exists()) {
            try {
                decFile.delete();
            } catch (Exception e) {
                log.error("检测图片删除失败, id: {}", id);
            }
        }

        imageMapper.deleteByPrimaryKey(id);

        return ResponseUtil.success(null);
    }

}

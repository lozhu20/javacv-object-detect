package com.random.carryyou.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图片信息表
 *
 * @TableName tb_image
 */
@Data
public class Image implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 图片名称
     */
    private String imageName;

    /**
     * 图片大小
     */
    private Integer size;

    /**
     * 存储路径
     */
    private String savePath;

    /**
     * 用途(01:品种检测,02:成熟度检测)
     */
    private String useAs;

    private String useAsDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    private String updatedBy;

    /**
     * 标识检测成功的图片是否存在，存在: Y ， 不存在: N
     */
    private String detectSuccess;

    private static final long serialVersionUID = 1L;

}

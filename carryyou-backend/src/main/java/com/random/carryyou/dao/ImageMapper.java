package com.random.carryyou.dao;

import com.random.carryyou.dto.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {

    int deleteByPrimaryKey(String id);

    int insert(Image record);

    int insertSelective(Image record);

    Image selectByPrimaryKey(String id);

    List<Image> selectByNameAndCreatedBy(@Param("imageName") String imageName,
                                         @Param("createdBy") String createdBy);

    int updateByPrimaryKeySelective(Image record);

    int updateByPrimaryKey(Image record);

}

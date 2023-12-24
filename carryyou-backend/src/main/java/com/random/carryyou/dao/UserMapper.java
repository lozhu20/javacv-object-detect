package com.random.carryyou.dao;

import com.random.carryyou.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User record);

    User selectByPrimaryKey(Long id);

    User selectByUserId(String userId);

    List<User> selectAll();

    int updateStatusByUserId(@Param("userId") String userId, @Param("status") String status);

    int updateUser(User user);
}

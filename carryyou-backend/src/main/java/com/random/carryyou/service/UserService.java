package com.random.carryyou.service;

import com.github.pagehelper.PageInfo;
import com.random.carryyou.dto.Response;
import com.random.carryyou.dto.User;

public interface UserService {

    Response<User> login(User user);

    Response<String> addUser(User user);

    Response<User> queryUser(String userId);

    Response<PageInfo<User>> queryAllUser(int pageNum, int pageSize);

    Response<Void> updateUserStatus(String userId, String status);

    Response<Void> updateUser(User user);

}

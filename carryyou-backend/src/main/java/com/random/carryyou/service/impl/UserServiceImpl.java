package com.random.carryyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.random.carryyou.constant.RoleEnum;
import com.random.carryyou.constant.StatusEnum;
import com.random.carryyou.dao.UserMapper;
import com.random.carryyou.dto.Response;
import com.random.carryyou.dto.User;
import com.random.carryyou.service.UserService;
import com.random.carryyou.utils.RequestUtil;
import com.random.carryyou.utils.ResponseUtil;
import com.random.carryyou.utils.TokenCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Response<User> login(User user) {
        if (user == null || StringUtils.isBlank(user.getUserId()) || StringUtils.isBlank(user.getPassword())) {
            return ResponseUtil.fail("用户名和密码不能为空");
        }
        String password = user.getPassword();
        User u = userMapper.selectByUserId(user.getUserId());
        if (u == null) {
            return ResponseUtil.fail("用户不存在");
        }

        if (!Objects.equals(StatusEnum.NORMAL.getCode(), u.getStatus())) {
            return ResponseUtil.fail("当前用户状态不允许登陆");
        }
        if (Objects.equals(password, u.getPassword())) {
            String token = UUID.randomUUID().toString();
            u.setToken(token);
            TokenCacheUtil.cacheToken(token);
            return ResponseUtil.success(u);
        } else {
            return ResponseUtil.fail("登陆信息错误");
        }
    }

    @Override
    public Response<String> addUser(User user) {
        if (user == null
                || StringUtils.isBlank(user.getUserId())
                || StringUtils.isBlank(user.getUsername())
                || StringUtils.isBlank(user.getPassword())) {
            return ResponseUtil.fail("信息不全");
        }

        String userId = user.getUserId();
        User u = userMapper.selectByUserId(userId);
        if (u != null) {
            return ResponseUtil.fail("用户id已存在");
        }

        user.setId(UUID.randomUUID().toString());
        user.setStatus(StatusEnum.NORMAL.getCode());
        if (StringUtils.isBlank(user.getRoleId())) {
            user.setRoleId(RoleEnum.ROLE_USER.getCode());
        }
        if(StringUtils.isBlank(user.getStatus())) {
            user.setStatus(StatusEnum.NORMAL.getCode());
        }
        user.setCreatedBy(RequestUtil.getCurrentUserId());
        user.setUpdatedBy(RequestUtil.getCurrentUserId());
        userMapper.insert(user);
        return ResponseUtil.success(null);
    }

    @Override
    public Response<User> queryUser(String userId) {
        User user = userMapper.selectByUserId(userId);
        user.setStatusDescription(StatusEnum.fromCode(user.getStatus()));
        user.setRoleName(RoleEnum.fromCode(user.getRoleId()));
        return ResponseUtil.success(user);
    }

    @Override
    public Response<PageInfo<User>> queryAllUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectAll();
        for (User user : userList) {
            user.setStatusDescription(StatusEnum.fromCode(user.getStatus()));
            user.setRoleName(RoleEnum.fromCode(user.getRoleId()));
        }
        return ResponseUtil.success(new PageInfo<>(userList));
    }

    @Override
    public Response<Void> updateUserStatus(String userId, String status) {
        String currentUserId = RequestUtil.getCurrentUserId();
        List<String> currentRoleList = RequestUtil.getCurrentRoleList();
        if (!Objects.equals(currentUserId, userId) && !currentRoleList.contains(RoleEnum.ROLE_ADMIN.getCode())) {
            return ResponseUtil.fail("没有权限");
        }

        userMapper.updateStatusByUserId(userId, status);

        return ResponseUtil.success(null);
    }

    @Override
    public Response<Void> updateUser(User user) {
        user.setUpdatedBy(RequestUtil.getCurrentUserId());
        userMapper.updateUser(user);

        return ResponseUtil.success(null);
    }
}

package com.random.carryyou.controller;

import com.github.pagehelper.PageInfo;
import com.random.carryyou.constant.StatusEnum;
import com.random.carryyou.dto.Response;
import com.random.carryyou.dto.User;
import com.random.carryyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    public Response<User> login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/user")
    public Response<String> register(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/user/list")
    public Response<PageInfo<User>> queryUser(@RequestParam int pageNum, @RequestParam int pageSize) {
        return userService.queryAllUser(pageNum, pageSize);
    }

    @GetMapping("/user/{userId}")
    public Response<User> queryUser(@PathVariable String userId) {
        return userService.queryUser(userId);
    }

    @PutMapping("/user/{userId}/{statusType}")
    public Response<Void> suspend(@PathVariable String userId, @PathVariable String statusType) {
        return userService.updateUserStatus(userId, statusType);
    }

    @PutMapping("/user/delete/{userId}")
    public Response<Void> delUser(@PathVariable String userId) {
        return userService.updateUserStatus(userId, StatusEnum.DELETE.getCode());
    }

    @PutMapping("/user")
    public Response<Void> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
}

package com.random.carryyou.dao;

import com.random.carryyou.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void insertTest() {
        User user = new User();
        user.setUserId("ZHU11");
        user.setUsername("用户test");
        user.setPassword("1234");
        user.setStatus("99");
        user.setCreatedBy("system");
        user.setUpdatedBy("system");
        userMapper.insert(user);
    }
}
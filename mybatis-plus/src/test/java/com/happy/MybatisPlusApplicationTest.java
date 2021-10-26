package com.happy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.happy.mapper.UserMapper;
import com.happy.pojo.User;
import com.happy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @author LJJ
 * @create 2021-10-22 21:00
 */
@SpringBootTest
public class MybatisPlusApplicationTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    void testMybatisPlus() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id",4,6,8,10);
        userService.remove(userQueryWrapper);
    }

    @Test
    void testInsert() {
        User user = new User();
        user.setName("zhongli");
        user.setAge(28);
        userMapper.insert(user);

    }

    @Test
    void testSelect() {
        List<User> userList = userService.list();
        System.out.println("666");
        System.out.println(userList);
    }

    @Test
    void testPage() {
        Page<User> userPage = new Page<>();
        userPage.setCurrent(0);
        userPage.setSize(3);
        List<User> userList = userMapper.selectPage(userPage, null).getRecords();
        System.out.println(userList);
    }
}

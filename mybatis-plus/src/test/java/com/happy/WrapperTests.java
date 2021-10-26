package com.happy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.happy.mapper.UserMapper;
import com.happy.pojo.User;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author LJJ
 * @create 2021-10-26 13:57
 */
@SpringBootTest
public class WrapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void test1(){
        //查询名字中包含n，年龄大于等于10且小于等于20，email不为空的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","n")
                    .between("age",10,22)
                    .isNotNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void test2(){
        //按年龄降序查询用户，如果年龄相同则按id降序排列
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age")
                    .orderByDesc("id");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void test3(){
        //删除email为空的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int result = userMapper.delete(queryWrapper);
        System.out.println("受影响的数量为："+result);
    }

    @Test
    void test4(){
        //查询名字中包含n，且（年龄小于18或email为空的用户），
        //并将这些用户的年龄设置为18，邮箱设置为 user@atguigu.com
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","n")
                    //.and(i -> i.lt("age",18).or().isNull("email"));
                    .and(new Consumer<QueryWrapper<User>>() {
                        @Override
                        public void accept(QueryWrapper<User> userQueryWrapper) {
                            userQueryWrapper.lt("age",18)
                                            .or()
                                            .isNull("email")
                                            .or()
                                            .eq("email","");
                        }
                    });
        User user = new User();
        user.setAge(18);
        user.setEmail("user@jason.com");
        int result = userMapper.update(user, queryWrapper);
        System.out.println("受影响的数量为："+result);
    }

    @Test
    void test5(){
        //查询所有用户的用户名和年龄
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name","age");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void test6(){
        //查询id不大于3的所有用户的id列表
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id",3);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void test8(){
        //查询名字中包含n，年龄大于10且小于20的用户，查询条件来源于用户输入，是可选的
        String name = null;
        Integer ageBegin = 10;
        Integer ageEnd = 20;

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),"name","n")
                .ge(ageBegin != null,"age",ageBegin)
                .le(ageEnd != null,"age",ageEnd);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }
}

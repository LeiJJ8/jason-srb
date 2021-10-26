package com.happy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.happy.mapper.UserMapper;
import com.happy.pojo.User;
import com.happy.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LJJ
 * @create 2021-10-25 14:07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public List<User> listAllByName(String name) {
        return baseMapper.selectAllByName(name);
    }
}

package com.happy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.happy.pojo.User;

import java.util.List;

/**
 * @author LJJ
 * @create 2021-10-25 13:58
 */
public interface UserService extends IService<User> {

    List<User> listAllByName(String name);
}

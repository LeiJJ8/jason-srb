package com.happy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.happy.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LJJ
 * @create 2021-10-22 20:59
 */
@Repository
public interface UserMapper extends BaseMapper<User>{

    List<User> selectAllByName(String name);
}

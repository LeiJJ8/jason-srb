package com.happy.srb.core;

import com.happy.srb.core.mapper.DictMapper;
import com.happy.srb.core.pojo.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author LeiJJ
 * @date 2021-11-03 8:57
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private DictMapper dictMapper;

    @Test
    public void saveDict(){
        Dict dict = dictMapper.selectById(1);
        redisTemplate.opsForValue().set("dict",dict);
    }

    @Test
    public void getDict(){
        //Dict dict = (Dict) redisTemplate.opsForValue().get("dict");
        String name = (String) redisTemplate.opsForValue().get("name");
        //System.out.println(dict);
        System.out.println(name);
        //redisTemplate.opsForValue().set("name","riven");

    }
}

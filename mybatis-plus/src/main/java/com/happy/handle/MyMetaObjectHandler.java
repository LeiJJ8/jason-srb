package com.happy.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author LJJ
 * @create 2021-10-25 20:40
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        strictInsertFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());

        Object age = getFieldValByName("age", metaObject);
        if(age == null){
            strictInsertFill(metaObject,"age",String.class,"18");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject,"updateTime",LocalDateTime.class,LocalDateTime.now());
    }
}

package com.happy.common.exception;

import com.happy.common.result.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author LeiJJ
 * @date 2021-10-29 18:23
 */
@Slf4j
public abstract class Assert {

    /**
     * 断言对象不为空
     * 如果对象obj为空，则抛出异常
     * @param obj 待判断对象
     * @param responseEnum 响应的信息
     */
    public static void nutNoll(Object obj, ResponseEnum responseEnum){
        if(obj == null){
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言对象为空
     * 如果对象obj不为空，则抛出异常
     * @param obj 待判断对象
     * @param responseEnum 响应的信息
     */
    public static void isNull(Object obj,ResponseEnum responseEnum){
        if(obj != null){
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言表达式为真
     * 如果表达式不为真，则抛出异常
     * @param expression 待判断的表达式
     * @param responseEnum 响应的信息
     */
    public static void isTrue(boolean expression,ResponseEnum responseEnum){
        if(!expression){
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言两个对象相等
     * 如果不相等，则抛出异常
     * @param o1
     * @param o2
     * @param responseEnum
     */
    public static void equals(Object o1,Object o2,ResponseEnum responseEnum){
        if(!o1.equals(o2)){
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言两个对象不相等
     * 如果相等，则抛出异常
     * @param o1
     * @param o2
     * @param responseEnum
     */
    public static void notEquals(Object o1,Object o2,ResponseEnum responseEnum){
        if(o1.equals(o2)){
            throw new BusinessException(responseEnum);
        }
    }

    /**
     * 断言参数不为空
     * 如果为空，则抛出异常
     * @param str 待判断参数
     * @param responseEnum
     */
    public static void notEmpty(String str,ResponseEnum responseEnum){
        if(StringUtils.isEmpty(str)){
            throw new BusinessException(responseEnum);
        }
    }
}

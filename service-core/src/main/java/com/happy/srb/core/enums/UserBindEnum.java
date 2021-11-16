package com.happy.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LeiJJ
 * @date 2021-11-10 8:48
 */
@AllArgsConstructor
@Getter
public enum UserBindEnum {

    NO_BIND(0,"未绑定"),
    BIND_OK(1,"绑定成功"),
    BIND_FAIL(-1,"绑定失败"),
    ;


    private final Integer status;
    private final String message;
}

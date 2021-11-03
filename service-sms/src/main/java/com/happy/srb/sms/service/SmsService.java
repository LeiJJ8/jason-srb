package com.happy.srb.sms.service;

import java.util.Map;

/**
 * @author LeiJJ
 * @date 2021-11-03 18:57
 */
public interface SmsService {

    void send(String mobile, String templateCode, Map<String,Object> param);
}

package com.happy.srb.sms.client.fallback;

import com.happy.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author LeiJJ
 * @date 2021-11-09 20:06
 */
@Slf4j
@Service
public class CoreUserInfoClientFallback implements CoreUserInfoClient {
    @Override
    public boolean checkMobile(String mobile) {
        log.error("远程端用失败，服务熔断");
        return false;
    }
}

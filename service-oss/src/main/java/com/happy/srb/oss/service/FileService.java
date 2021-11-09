package com.happy.srb.oss.service;

import java.io.InputStream;

/**
 * @author LeiJJ
 * @date 2021-11-05 19:30
 */
public interface FileService {
    /**
     * 文件上传
     */
    String upload(InputStream inputStream,String module,String fileName);

    /**
     * 文件删除
     */
    void removeFile(String url);
}

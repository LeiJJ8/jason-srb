package com.happy.srb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.happy.srb.oss.service.FileService;
import com.happy.srb.oss.utils.OssYml;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author LeiJJ
 * @date 2021-11-05 19:32
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(InputStream inputStream, String module, String fileName) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = OssYml.ENDPOINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = OssYml.KEY_ID;
        String accessKeySecret = OssYml.KEY_SECRET;
        String bucketName = OssYml.BUCKET_NAME;

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        if(!ossClient.doesBucketExist(bucketName)){
            ossClient.createBucket("ljj-srb-file");
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
        //构建日期路径：avatar/2021/11/05/文件名
        String date = new DateTime().toString("yy/MM/dd");

        fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));

        String key = module + "/" + date + "/" + fileName;

        ossClient.putObject(bucketName,key,inputStream);

        ossClient.shutdown();

        return "https://" + bucketName + "." + endpoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = OssYml.ENDPOINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = OssYml.KEY_ID;
        String accessKeySecret = OssYml.KEY_SECRET;
        String bucketName = OssYml.BUCKET_NAME;

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String host = "https://" + bucketName + "." + endpoint + "/";
        String objectName = url.substring(host.length());

        ossClient.deleteObject(bucketName,objectName);

        ossClient.shutdown();
    }
}

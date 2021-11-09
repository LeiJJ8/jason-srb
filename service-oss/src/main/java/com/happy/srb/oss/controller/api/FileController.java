package com.happy.srb.oss.controller.api;

import com.happy.common.exception.BusinessException;
import com.happy.common.result.ResponseEnum;
import com.happy.common.result.Result;
import com.happy.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author LeiJJ
 * @date 2021-11-05 19:58
 */
@Api(tags = "阿里云文件管理")
@CrossOrigin
@RestController
@RequestMapping("/api/oss/file")
public class FileController {

    @Resource
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result upload(@ApiParam(value = "文件",required = true)
                         @RequestParam("file")MultipartFile file,

                         @ApiParam(value = "模块",required = true)
                         @RequestParam("module") String module) throws IOException {

        try {
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            String url = fileService.upload(inputStream, module, filename);
            return Result.ok().data("url",url);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }

    @ApiOperation("文件删除")
    @DeleteMapping("/remove")
    public Result remove(@ApiParam("要删除的文件路径")@RequestParam("url") String url){
        fileService.removeFile(url);
        return Result.ok().message("删除成功");
    }
}

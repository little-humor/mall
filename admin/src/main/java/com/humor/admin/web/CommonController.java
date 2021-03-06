package com.humor.admin.web;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author zhangshaoze
 * @date 2018/12/4 9:07 PM
 */
@Api("公共接口")
@RestController
@RequestMapping("common")
public class CommonController {

    @Autowired
    private IFileService fileService;

    @ApiOperation(value = "图片上传",notes = "文件系统fastDFS")
    @PostMapping("uploadImages.do")
    public ServerResponse uploadImages(HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        return fileService.uploadImages(files);
    }


}

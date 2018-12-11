package com.humor.admin.service;

import com.humor.admin.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangshaoze
 * @create 2018-12-04 10:49 PM
 */
public interface IFileService {

    /**
     * 上传文件
     * @param files
     * @return
     * @throws IOException
     */
    ServerResponse uploadImages(List<MultipartFile> files) throws IOException;
}

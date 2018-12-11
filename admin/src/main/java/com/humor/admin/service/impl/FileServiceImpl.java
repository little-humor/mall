package com.humor.admin.service.impl;

import com.humor.admin.common.ServerResponse;
import com.humor.admin.common.fastdfs.FastDFSClientWrapper;
import com.humor.admin.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author zhangshaoze
 * @date 2018/12/4 10:49 PM
 */
@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;

    @Override
    public ServerResponse uploadImages(List<MultipartFile> files) throws IOException {
        MultipartFile file = null;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<files.size();i++){
            file = files.get(i);
            if(file.isEmpty()){
                return ServerResponse.createBySuccessMessage("第【"+i+"】张图片不存在");
            }
            //上传到文件系统
            String path = fastDFSClientWrapper.uploadFile(file);

            //拼接图片地址
            if(i>0){
                sb.append(",");
            }
            sb.append(path);

        }
        return ServerResponse.createBySuccess("图片上传成功",sb.toString());
    }

}

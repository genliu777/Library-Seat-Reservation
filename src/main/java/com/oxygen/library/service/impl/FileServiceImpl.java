package com.oxygen.library.service.impl;

import com.oxygen.library.dto.Response;
import com.oxygen.library.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * 文件按上传下载服务实现类
 */
@Service
public class FileServiceImpl implements FileService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${common.upload}")
    String path;

    @Override
    public Response upload(MultipartFile file) throws Exception {
        if(file.isEmpty()){
            logger.info("空文件");
            return new Response(200,"OK", "上传失败");
        }

        try {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            // 取得文件后缀
            String type = fileName.substring(fileName.lastIndexOf("."));
            logger.info("上传文件名：" + fileName + "大小：" + (int) file.getSize());
            // 时间戳作为文件名
            long timestamp = System.currentTimeMillis();
            File dest = new File(path + "/" + timestamp + type);

            // 判断文件父目录是否存在
            if(!dest.getParentFile().exists()){
                boolean res = dest.getParentFile().mkdir();
                if (res) logger.info("创建目录成功");
                else logger.info("创建目录失败");
            }

            //保存文件
            file.transferTo(dest);
            logger.info("文件保存成功，路径：" + dest.getPath());
            return new Response(200, "OK",dest.getPath());
        } catch (IllegalStateException | IOException | ArrayIndexOutOfBoundsException e) {
            logger.debug("发生异常");
            e.printStackTrace();
            return new Response(200, "OK", "上传失败");
        }
    }

    @Override
    // 暂时用不到，未实现
    public Response download(HttpServletRequest request) throws Exception {
        return null;
    }
}

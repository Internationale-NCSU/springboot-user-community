package com.dareway.jc.content.community.feign;

import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.pojo.FileUploadResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lichp
 * @version 1.0.0  2021/5/6 15:29
 * @since JDK1.8
 */
@FeignClient(name = "jc-business-file", path = "/ftp")
public interface FileFeignService {


    /**
     * 上传到ftp服务器上
     *
     * @param file 文件
     * @param url  服务器地址
     * @return
     */
    @PostMapping(value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传到ftp服务器上", httpMethod = "POST")
    R<FileUploadResult> uploadByFile(@RequestPart(value = "file") MultipartFile file, @RequestParam("url") String url);
}

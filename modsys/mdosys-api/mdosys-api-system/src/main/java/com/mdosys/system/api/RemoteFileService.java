package com.mdosys.system.api;


import com.mdosys.system.api.factory.RemoteFileFallbackFactory;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 文件服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteFileService", value = "mdosys-file", fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService {

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam("bucket") String bucket);

    @PostMapping(value = "/deleteFile")
    public void deleteFile(@RequestParam("path") String path);

    @GetMapping(value = "/downloadFile")
    public Response download(@RequestParam("path") String path);

    @GetMapping(value = "/getInputStream")
    public Response getInputStream(@RequestParam("path") String path);

}

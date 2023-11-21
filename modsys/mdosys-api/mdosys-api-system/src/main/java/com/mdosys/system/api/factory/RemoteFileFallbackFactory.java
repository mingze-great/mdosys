package com.mdosys.system.api.factory;

import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.system.api.RemoteFileService;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * 文件服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable)
    {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService()
        {

            @Override
            public String uploadFile(MultipartFile file, String bucket) {
                return null;
            }

            @Override
            public void deleteFile(String path) {
            }

            @Override
            public Response download(String path) {
                return null;
            }

            @Override
            public Response getInputStream(String path) {
                return null;
            }

        };
    }
}

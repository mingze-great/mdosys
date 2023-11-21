package com.mdosys.system.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mdosys.common.core.enums.Paraminfo;
import com.mdosys.common.core.utils.StringUtils;
import com.mdosys.common.core.utils.file.FileSizeUtils;
import com.mdosys.common.core.utils.file.FileTypeUtils;
import com.mdosys.common.core.utils.file.FileUtils;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.common.core.web.page.TableDataInfo;
import com.mdosys.system.api.RemoteFileService;
import com.mdosys.system.domain.SysComponentFile;
import com.mdosys.system.domain.SysSpaceFile;
import com.mdosys.system.mapper.SysParamInfoMapper;
import com.mdosys.system.service.ISysComponentFileService;
import com.mdosys.system.service.ISysParamInfoService;
import feign.Response;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.mdosys.common.core.utils.file.ReadNormalFile.readParams;

/**
 * 组件文件Controller
 * 
 * @author hwj
 * @date 2022-10-25
 */
@RestController
@RequestMapping("/componentFile")
public class SysComponentFileController extends BaseController
{
    @Autowired
    private ISysComponentFileService sysComponentFileService;

    @Autowired
    private RemoteFileService fileService;

    @Autowired
    private ISysParamInfoService paramInfoService;

    @Value("${minio.compBucket}")
    private String bucket;

    /**
     * 查询组件文件列表
     * @param id 组件id
     * @return
     */
    @GetMapping("/fileList/{id}")
    public AjaxResult fileList(@PathVariable("id") Long id) {
        SysComponentFile componentFile = new SysComponentFile();
        componentFile.setCompId(id);
        List<SysComponentFile> list = sysComponentFileService.selectSysComponentFileList(componentFile);
        return AjaxResult.success(list);
    }


    /**
     * 获取组件文件详细信息
     */
//    @RequiresPermissions("system:file:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysComponentFileService.selectSysComponentFileById(id));
    }

    /**
     * 新增组件文件
     */
//    @RequiresPermissions("system:file:add")
//    @Log(title = "组件文件", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(SysComponentFile sysComponentFile, HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        for (MultipartFile file : files) {
            String path = fileService.uploadFile(file, bucket);
            if (StringUtils.isEmpty(path)){
                return AjaxResult.error("文件上传失败！");
            }
            sysComponentFile.setName(file.getOriginalFilename());
            sysComponentFile.setPath(path);
            sysComponentFile.setSize(FileSizeUtils.getFileSize(file.getSize()));
            sysComponentFileService.insertSysComponentFile(sysComponentFile);
        }
        return AjaxResult.success("上传成功！");
    }

    /**
     * 修改组件文件
     */
    @PutMapping(value = "/update")
    public AjaxResult edit(@RequestBody SysComponentFile sysComponentFile) {
        return toAjax(sysComponentFileService.updateSysComponentFile(sysComponentFile));
    }

    /**
     * 修改组件文件类型
     */
    @PutMapping(value = "/updateType")
    public AjaxResult editType(@RequestBody SysComponentFile sysComponentFile) throws IOException {
        if ("输入文件".equals(sysComponentFile.getType())){
            String path = sysComponentFile.getPath();
            if (path.indexOf(':') == -1)    return AjaxResult.error("文件的路径不正确，请联系管理员查看！");
            Response responseFile =  fileService.download(path);
            InputStream inputStream = responseFile.body().asInputStream();
            List<Paraminfo> list = readParams(inputStream);
            for (Paraminfo p : list) {
                p.setFileId(sysComponentFile.getId());
                paramInfoService.insertSysParamInfo(p);
            }
        }
        return toAjax(sysComponentFileService.updateSysComponentFileType(sysComponentFile));
    }

    /**
     * 删除组件文件
     */
//    @RequiresPermissions("system:file:remove")
//    @Log(title = "组件文件", businessType = BusinessType.DELETE)
	@PostMapping(value = "/del")
    public AjaxResult remove(@RequestBody SysComponentFile sysComponentFile) {
        String path = sysComponentFile.getPath();
        if (path.indexOf(':') == -1)    return AjaxResult.error("文件的路径不正确，请联系管理员查看！");
        fileService.deleteFile(path);
        return toAjax(sysComponentFileService.deleteSysComponentFileById(sysComponentFile.getId()));
    }

    /**
     * 下载组件文件
     * @param sysComponentFile 文件信息
     * @return
     */
    @PostMapping(value = "/download")
    public AjaxResult downloadFile(@RequestBody SysComponentFile sysComponentFile, HttpServletResponse response) {
        String path = sysComponentFile.getPath();
        if (path.indexOf(':') == -1)    return AjaxResult.error("路径的格式不正确，请联系管理员查看！");
        try{
            Response responseFile =  fileService.download(path);
            InputStream inputStream = responseFile.body().asInputStream();
            FileUtils.writeFileToRes(response, sysComponentFile.getName(), inputStream);
        } catch (IOException e){
            return AjaxResult.error("文件下载失败，请联系管理员查看！");
        }
        return null;
    }

    /**
     * MultipartFile 转 File
     *
     * @param multipartFile
     * @throws Exception
     */
    public static File MultipartFileToFile(MultipartFile multipartFile) {

        File file = null;
        //判断是否为null
        if (multipartFile.equals("") || multipartFile.getSize() <= 0) {
            return file;
        }
        //MultipartFile转换为File
        InputStream ins = null;
        OutputStream os = null;
        try {
            ins = multipartFile.getInputStream();
            file = new File(multipartFile.getOriginalFilename());
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ins != null){
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}

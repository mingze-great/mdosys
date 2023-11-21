package com.mdosys.system.controller;

import com.github.pagehelper.PageInfo;
import com.mdosys.common.core.utils.StringUtils;
import com.mdosys.common.core.utils.file.FileSizeUtils;
import com.mdosys.common.core.utils.file.FileTypeUtils;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.common.core.web.page.TableDataInfo;
import com.mdosys.system.api.RemoteFileService;
import com.mdosys.system.domain.SysSpaceFile;
import com.mdosys.system.service.ISysSpaceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 文件Controller
 * 
 * @author hwj
 * @date 2022-10-11
 */
@RestController
@RequestMapping("/spaceFile")
public class SysSpaceFileController extends BaseController
{
    @Autowired
    private ISysSpaceFileService sysFileService;

    @Autowired
    private RemoteFileService fileService;

    @Value("${minio.spaceBucket}")
    private String bucket;

    /**
     * 查询文件列表
     */
//    @RequiresPermissions("system:file:list")
    @GetMapping("/list")
    public AjaxResult list(SysSpaceFile sysSpaceFile) {
        startPage();
        List<SysSpaceFile> list = sysFileService.selectSysSpaceFileList(sysSpaceFile);
        PageInfo<SysSpaceFile> pageInfo = new PageInfo<>(list);
        return AjaxResult.success(pageInfo);
    }

    /**
     * 获取文件详细信息
     */
//    @RequiresPermissions("system:file:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysFileService.selectSysSpaceFileById(id));
    }

    /**
     * 上传文件
     */
//    @RequiresPermissions("system:file:add")
//    @Log(title = "文件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(SysSpaceFile sys_space_file, HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        for (MultipartFile file : files) {
            String path = fileService.uploadFile(file, bucket);
            if (StringUtils.isEmpty(path)){
                return AjaxResult.error("文件上传失败！");
            }
            sys_space_file.setName(file.getOriginalFilename());
            sys_space_file.setSize(FileSizeUtils.getFileSize(file.getSize()));
            sys_space_file.setCreateTime(new Date());
            sys_space_file.setFileType("F");
            sys_space_file.setPath(path);
            sysFileService.insertSysSpaceFile(sys_space_file);
        }
        return AjaxResult.success("添加成功！");
    }

//    @Log(title = "文件", businessType = BusinessType.INSERT)
    @PostMapping("/addDir")
    public AjaxResult addDir(@RequestBody SysSpaceFile sys_space_file) {
        sys_space_file.setUserId((long) 1);
        sys_space_file.setPath("-");
        sys_space_file.setFileType("D");
        sys_space_file.setType("-");
        sys_space_file.setSize("-");
        sys_space_file.setCreateTime(new Date());
        sysFileService.insertSysSpaceFile(sys_space_file);
        return AjaxResult.success("添加成功！");
    }

    /**
     * 修改文件
     */
//    @RequiresPermissions("system:file:edit")
//    @Log(title = "文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSpaceFile sysFile)
    {
        return toAjax(sysFileService.updateSysSpaceFile(sysFile));
    }

    /**
     * 删除文件
     */
//    @RequiresPermissions("system:file:remove")
//    @Log(title = "文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysFileService.deleteSysSpaceFileByIds(ids));
    }



}

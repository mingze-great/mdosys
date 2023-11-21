package com.mdosys.system.service.impl;

import java.util.List;

import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.system.domain.SysComponentFile;
import com.mdosys.system.mapper.SysComponentFileMapper;
import com.mdosys.system.service.ISysComponentFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 组件文件Service业务层处理
 * 
 * @author hwj
 * @date 2022-10-25
 */
@Service
public class SysComponentFileServiceImpl implements ISysComponentFileService
{
    @Autowired
    private SysComponentFileMapper sysComponentFileMapper;

    /**
     * 查询组件文件
     * 
     * @param id 组件文件主键
     * @return 组件文件
     */
    @Override
    public SysComponentFile selectSysComponentFileById(Long id)
    {
        return sysComponentFileMapper.selectSysComponentFileById(id);
    }

    /**
     * 查询组件文件列表
     * 
     * @param sysComponentFile 组件文件
     * @return 组件文件
     */
    @Override
    public List<SysComponentFile> selectSysComponentFileList(SysComponentFile sysComponentFile)
    {
        return sysComponentFileMapper.selectSysComponentFileList(sysComponentFile);
    }

    /**
     * 新增组件文件
     * 
     * @param sysComponentFile 组件文件
     * @return 结果
     */
    @Override
    public int insertSysComponentFile(SysComponentFile sysComponentFile)
    {
        sysComponentFile.setCreateTime(DateUtils.getNowDate());
        return sysComponentFileMapper.insertSysComponentFile(sysComponentFile);
    }

    /**
     * 修改组件文件
     * 
     * @param sysComponentFile 组件文件
     * @return 结果
     */
    @Override
    public int updateSysComponentFile(SysComponentFile sysComponentFile)
    {
        return sysComponentFileMapper.updateSysComponentFile(sysComponentFile);
    }

    /**
     * 批量删除组件文件
     * 
     * @param ids 需要删除的组件文件主键
     * @return 结果
     */
    @Override
    public int deleteSysComponentFileByIds(Long[] ids)
    {
        return sysComponentFileMapper.deleteSysComponentFileByIds(ids);
    }

    /**
     * 删除组件文件信息
     * 
     * @param id 组件文件主键
     * @return 结果
     */
    @Override
    public int deleteSysComponentFileById(Long id) {
        return sysComponentFileMapper.deleteSysComponentFileById(id);
    }

    /**
     * 根据组件id删除文件
     * @param id
     * @return
     */
    @Override
    public int deleteSysComponentFileByCompId(Long id){
        return sysComponentFileMapper.deleteSysComponentFileByCompId(id);
    }

    @Override
    public List<String> selectFilePathsByCompId(Long compId) {
        return sysComponentFileMapper.selectFilePathsByCompId(compId);
    }

    @Override
    public int updateSysComponentFileType(SysComponentFile sysComponentFile) {
        return sysComponentFileMapper.updateSysComponentFileType(sysComponentFile);
    }

    @Override
    public SysComponentFile selectSysComponentFilePath(Long id){
        return sysComponentFileMapper.selectSysComponentFilePath(id);
    }
}

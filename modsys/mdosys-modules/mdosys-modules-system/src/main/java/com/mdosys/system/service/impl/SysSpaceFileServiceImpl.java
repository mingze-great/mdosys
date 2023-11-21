package com.mdosys.system.service.impl;

import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.system.domain.SysSpaceFile;
import com.mdosys.system.mapper.SysSpaceFileMapper;
import com.mdosys.system.service.ISysSpaceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件Service业务层处理
 * 
 * @author hwj
 * @date 2022-10-11
 */
@Service
public class SysSpaceFileServiceImpl implements ISysSpaceFileService
{
    @Autowired
    private SysSpaceFileMapper sysSpaceFileMapper;

    /**
     * 查询文件
     * 
     * @param id 文件主键
     * @return 文件
     */
    @Override
    public SysSpaceFile selectSysSpaceFileById(Long id) {
        return sysSpaceFileMapper.selectSysSpaceFileById(id);
    }

    /**
     * 查询文件列表
     * 
     * @param sysSpaceFile 文件
     * @return 文件
     */
    @Override
    public List<SysSpaceFile> selectSysSpaceFileList(SysSpaceFile sysSpaceFile) {
        return sysSpaceFileMapper.selectSysSpaceFileList(sysSpaceFile);
    }

    /**
     * 新增文件
     * 
     * @param sysSpaceFile 文件
     * @return 结果
     */
    @Override
    public int insertSysSpaceFile(SysSpaceFile sysSpaceFile)
    {
        sysSpaceFile.setCreateTime(DateUtils.getNowDate());
        return sysSpaceFileMapper.insertSysSpaceFile(sysSpaceFile);
    }

    /**
     * 修改文件
     * 
     * @param sysSpaceFile 文件
     * @return 结果
     */
    @Override
    public int updateSysSpaceFile(SysSpaceFile sysSpaceFile)
    {
        return sysSpaceFileMapper.updateSysSpaceFile(sysSpaceFile);
    }

    /**
     * 批量删除文件
     * 
     * @param ids 需要删除的文件主键
     * @return 结果
     */
    @Override
    public int deleteSysSpaceFileByIds(Long[] ids)
    {
        return sysSpaceFileMapper.deleteSysSpaceFileByIds(ids);
    }

    /**
     * 删除文件信息
     * 
     * @param id 文件主键
     * @return 结果
     */
    @Override
    public int deleteSysSpaceFileById(Long id)
    {
        return sysSpaceFileMapper.deleteSysSpaceFileById(id);
    }
}

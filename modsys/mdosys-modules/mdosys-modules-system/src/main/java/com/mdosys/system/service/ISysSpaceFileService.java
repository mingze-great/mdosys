package com.mdosys.system.service;

import com.mdosys.system.domain.SysSpaceFile;

import java.util.List;

/**
 * 文件Service接口
 * 
 * @author hwj
 * @date 2022-10-11
 */
public interface ISysSpaceFileService
{
    /**
     * 查询文件
     * 
     * @param id 文件主键
     * @return 文件
     */
    public SysSpaceFile selectSysSpaceFileById(Long id);

    /**
     * 查询文件列表
     * 
     * @param sysSpaceFile 文件
     * @return 文件集合
     */
    public List<SysSpaceFile> selectSysSpaceFileList(SysSpaceFile sysSpaceFile);

    /**
     * 新增文件
     * 
     * @param sysSpaceFile 文件
     * @return 结果
     */
    public int insertSysSpaceFile(SysSpaceFile sysSpaceFile);

    /**
     * 修改文件
     * 
     * @param sysSpaceFile 文件
     * @return 结果
     */
    public int updateSysSpaceFile(SysSpaceFile sysSpaceFile);

    /**
     * 批量删除文件
     * 
     * @param ids 需要删除的文件主键集合
     * @return 结果
     */
    public int deleteSysSpaceFileByIds(Long[] ids);

    /**
     * 删除文件信息
     * 
     * @param id 文件主键
     * @return 结果
     */
    public int deleteSysSpaceFileById(Long id);
}

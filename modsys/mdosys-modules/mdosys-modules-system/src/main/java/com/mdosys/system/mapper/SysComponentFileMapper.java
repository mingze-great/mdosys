package com.mdosys.system.mapper;

import com.mdosys.system.domain.SysComponentFile;

import java.util.List;

/**
 * 组件文件Mapper接口
 * 
 * @author hwj
 * @date 2022-10-25
 */
public interface SysComponentFileMapper 
{
    /**
     * 查询组件文件
     * 
     * @param id 组件文件主键
     * @return 组件文件
     */
    public SysComponentFile selectSysComponentFileById(Long id);

    /**
     * 查询组件文件列表
     * 
     * @param sysComponentFile 组件文件
     * @return 组件文件集合
     */
    public List<SysComponentFile> selectSysComponentFileList(SysComponentFile sysComponentFile);

    /**
     * 新增组件文件
     * 
     * @param sysComponentFile 组件文件
     * @return 结果
     */
    public int insertSysComponentFile(SysComponentFile sysComponentFile);

    /**
     * 修改组件文件
     * 
     * @param sysComponentFile 组件文件
     * @return 结果
     */
    public int updateSysComponentFile(SysComponentFile sysComponentFile);

    /**
     * 删除组件文件
     * 
     * @param id 组件文件主键
     * @return 结果
     */
    public int deleteSysComponentFileById(Long id);

    /**
     * 批量删除组件文件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysComponentFileByIds(Long[] ids);


    /**
     * 根据comp_id删除文件
     * @param id
     * @return
     */
    public int deleteSysComponentFileByCompId(Long id);

    /**
     * 根据组件id获取文件路径
     * @param compId 组件id
     * @return
     */
    public List<String> selectFilePathsByCompId(Long compId);

    /**
     * 根据id修改文件类型
     * @param sysComponentFile
     * @return
     */
    public int updateSysComponentFileType(SysComponentFile sysComponentFile);

    SysComponentFile selectSysComponentFilePath(Long id);
}

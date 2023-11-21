package com.mdosys.system.service;

import java.util.List;
import com.mdosys.system.domain.SysEnvironment;

/**
 * 环境Service接口
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
public interface ISysEnvironmentService
{
    /**
     * 查询环境
     *
     * @param envirId 环境主键
     * @return 环境
     */
    public SysEnvironment selectSysEnvironmentByEnvirId(Long envirId);

    /**
     * 查询环境列表
     *
     * @param sysEnvironment 环境
     * @return 环境集合
     */
    public List<SysEnvironment> selectSysEnvironmentList(SysEnvironment sysEnvironment);

    /**
     * 新增环境
     *
     * @param sysEnvironment 环境
     * @return 结果
     */
    public int insertSysEnvironment(SysEnvironment sysEnvironment);

    /**
     * 修改环境
     *
     * @param sysEnvironment 环境
     * @return 结果
     */
    public int updateSysEnvironment(SysEnvironment sysEnvironment);

    /**
     * 批量删除环境
     *
     * @param envirIds 需要删除的环境主键集合
     * @return 结果
     */
    public int deleteSysEnvironmentByEnvirIds(Long[] envirIds);

    /**
     * 删除环境信息
     *
     * @param envirId 环境主键
     * @return 结果
     */
    public int deleteSysEnvironmentByEnvirId(Long envirId);
}

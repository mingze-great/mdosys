package com.mdosys.system.service.impl;

import java.util.List;
import com.mdosys.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mdosys.system.mapper.SysEnvironmentMapper;
import com.mdosys.system.domain.SysEnvironment;
import com.mdosys.system.service.ISysEnvironmentService;

/**
 * 环境Service业务层处理
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
@Service
public class SysEnvironmentServiceImpl implements ISysEnvironmentService
{
    @Autowired
    private SysEnvironmentMapper sysEnvironmentMapper;

    /**
     * 查询环境
     *
     * @param envirId 环境主键
     * @return 环境
     */
    @Override
    public SysEnvironment selectSysEnvironmentByEnvirId(Long envirId)
    {
        return sysEnvironmentMapper.selectSysEnvironmentByEnvirId(envirId);
    }

    /**
     * 查询环境列表
     *
     * @param sysEnvironment 环境
     * @return 环境
     */
    @Override
    public List<SysEnvironment> selectSysEnvironmentList(SysEnvironment sysEnvironment)
    {
        return sysEnvironmentMapper.selectSysEnvironmentList(sysEnvironment);
    }

    /**
     * 新增环境
     *
     * @param sysEnvironment 环境
     * @return 结果
     */
    @Override
    public int insertSysEnvironment(SysEnvironment sysEnvironment)
    {
        sysEnvironment.setCreateTime(DateUtils.getNowDate());
        return sysEnvironmentMapper.insertSysEnvironment(sysEnvironment);
    }

    /**
     * 修改环境
     *
     * @param sysEnvironment 环境
     * @return 结果
     */
    @Override
    public int updateSysEnvironment(SysEnvironment sysEnvironment)
    {
        sysEnvironment.setUpdateTime(DateUtils.getNowDate());
        return sysEnvironmentMapper.updateSysEnvironment(sysEnvironment);
    }

    /**
     * 批量删除环境
     *
     * @param envirIds 需要删除的环境主键
     * @return 结果
     */
    @Override
    public int deleteSysEnvironmentByEnvirIds(Long[] envirIds)
    {
        return sysEnvironmentMapper.deleteSysEnvironmentByEnvirIds(envirIds);
    }

    /**
     * 删除环境信息
     *
     * @param envirId 环境主键
     * @return 结果
     */
    @Override
    public int deleteSysEnvironmentByEnvirId(Long envirId)
    {
        return sysEnvironmentMapper.deleteSysEnvironmentByEnvirId(envirId);
    }
}

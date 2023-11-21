package com.mdosys.system.service.impl;

import java.util.List;
import com.mdosys.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mdosys.system.mapper.SysWorkerMapper;
import com.mdosys.system.domain.SysWorker;
import com.mdosys.system.service.ISysWorkerService;

/**
 * worker分组Service业务层处理
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
@Service
public class SysWorkerServiceImpl implements ISysWorkerService
{
    @Autowired
    private SysWorkerMapper sysWorkerMapper;

    /**
     * 查询worker分组
     *
     * @param workerId worker分组主键
     * @return worker分组
     */
    @Override
    public SysWorker selectSysWorkerByWorkerId(Long workerId)
    {
        return sysWorkerMapper.selectSysWorkerByWorkerId(workerId);
    }

    /**
     * 查询worker分组列表
     *
     * @param sysWorker worker分组
     * @return worker分组
     */
    @Override
    public List<SysWorker> selectSysWorkerList(SysWorker sysWorker)
    {
        return sysWorkerMapper.selectSysWorkerList(sysWorker);
    }

    /**
     * 新增worker分组
     *
     * @param sysWorker worker分组
     * @return 结果
     */
    @Override
    public int insertSysWorker(SysWorker sysWorker)
    {
        sysWorker.setCreateTime(DateUtils.getNowDate());
        return sysWorkerMapper.insertSysWorker(sysWorker);
    }

    /**
     * 修改worker分组
     *
     * @param sysWorker worker分组
     * @return 结果
     */
    @Override
    public int updateSysWorker(SysWorker sysWorker)
    {
        sysWorker.setUpdateTime(DateUtils.getNowDate());
        return sysWorkerMapper.updateSysWorker(sysWorker);
    }

    /**
     * 批量删除worker分组
     *
     * @param workerIds 需要删除的worker分组主键
     * @return 结果
     */
    @Override
    public int deleteSysWorkerByWorkerIds(Long[] workerIds)
    {
        return sysWorkerMapper.deleteSysWorkerByWorkerIds(workerIds);
    }

    /**
     * 删除worker分组信息
     *
     * @param workerId worker分组主键
     * @return 结果
     */
    @Override
    public int deleteSysWorkerByWorkerId(Long workerId)
    {
        return sysWorkerMapper.deleteSysWorkerByWorkerId(workerId);
    }
}

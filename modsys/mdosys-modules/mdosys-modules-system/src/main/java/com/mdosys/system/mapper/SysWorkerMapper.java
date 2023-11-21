package com.mdosys.system.mapper;

import java.util.List;
import com.mdosys.system.domain.SysWorker;

/**
 * worker分组Mapper接口
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
public interface SysWorkerMapper
{
    /**
     * 查询worker分组
     *
     * @param workerId worker分组主键
     * @return worker分组
     */
    public SysWorker selectSysWorkerByWorkerId(Long workerId);

    /**
     * 查询worker分组列表
     *
     * @param sysWorker worker分组
     * @return worker分组集合
     */
    public List<SysWorker> selectSysWorkerList(SysWorker sysWorker);

    /**
     * 新增worker分组
     *
     * @param sysWorker worker分组
     * @return 结果
     */
    public int insertSysWorker(SysWorker sysWorker);

    /**
     * 修改worker分组
     *
     * @param sysWorker worker分组
     * @return 结果
     */
    public int updateSysWorker(SysWorker sysWorker);

    /**
     * 删除worker分组
     *
     * @param workerId worker分组主键
     * @return 结果
     */
    public int deleteSysWorkerByWorkerId(Long workerId);

    /**
     * 批量删除worker分组
     *
     * @param workerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysWorkerByWorkerIds(Long[] workerIds);
}

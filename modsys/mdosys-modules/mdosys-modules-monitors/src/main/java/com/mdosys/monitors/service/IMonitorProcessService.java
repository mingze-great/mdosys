package com.mdosys.monitors.service;



import com.mdosys.monitors.domain.MonitorProcessItem;

import java.util.List;

/**
 * 流程监控Service接口
 */
public interface IMonitorProcessService {

    /**
     * 查询流程列表
     * @param monitorProcessItem 流程
     * @return 流程集合
     */
    public List<MonitorProcessItem> selectMonitorProcessList(MonitorProcessItem monitorProcessItem);

    public MonitorProcessItem selectMonitorProgressById(MonitorProcessItem monitorProcessItem);

    /**
     *修改某条流程
     * @param monitorProcessItem
     * @return 结果
     */
    public int updateMonitorProcess(MonitorProcessItem monitorProcessItem);



    /**
     * 批量修改流程(关闭或开始)
     * @param monitorProcessItem
     * @return
     */
    public int updateMonitorProcessByIds(MonitorProcessItem monitorProcessItem);

    public int countMonitorProcess(MonitorProcessItem monitorProcessItem);



}

package com.mdosys.monitors.mapper;


import com.mdosys.monitors.domain.MonitorProcessItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 流程监控 mapper接口
 */
@Mapper
public interface MonitorProcessMapper {

    /**
     * 查询流程列表
     * @param monitorProcessItem
     * @return 结果
     */
    public List<MonitorProcessItem> selectMonitorProcessList(MonitorProcessItem monitorProcessItem);


    /**
     * 修改某条流程信息(开始或者终止)
     * @param monitorProcessItem
     * @return 结果
     */
    public int updateMonitorProcess(MonitorProcessItem monitorProcessItem);

    public int countMonitorProcess(MonitorProcessItem monitorProcessItem);


    public MonitorProcessItem selectMonitorProgressById(MonitorProcessItem monitorProcessItem);

//    public int updateMonitorProcessByIds();//这个暂时不考虑，前端直接循环调用单条的






}

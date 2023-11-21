package com.mdosys.monitors.service.impl;


import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.monitors.domain.MonitorProcessItem;
import com.mdosys.monitors.mapper.MonitorProcessMapper;
import com.mdosys.monitors.service.IMonitorProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 流程监控Service业务层处理
 */
@Service
public class MonitorProcessServiceImpl implements IMonitorProcessService {


    @Autowired
    private MonitorProcessMapper monitorProcessMapper;



    /**
     * 查询流程列表
     * @param monitorProcessItem 流程
     * @return 流程列表
     */
    @Override
    public List<MonitorProcessItem> selectMonitorProcessList(MonitorProcessItem monitorProcessItem) {
        return monitorProcessMapper.selectMonitorProcessList(monitorProcessItem);
    }

    @Override
    public MonitorProcessItem selectMonitorProgressById(MonitorProcessItem monitorProcessItem) {
        return monitorProcessMapper.selectMonitorProgressById(monitorProcessItem);
    }

    /**
     * 更改流程状态
     * @param monitorProcessItem
     * @return 结果
     */
    @Override
    public int updateMonitorProcess(MonitorProcessItem monitorProcessItem) {
        if(monitorProcessItem.isRunFlag()){//开始运行
            monitorProcessItem.setStartTime(DateUtils.getNowDate());
        }
        else{//终止运行
            monitorProcessItem.setEndTime(DateUtils.getNowDate());
            monitorProcessItem.setRunTime(DateUtils.getDatePoor(monitorProcessItem.getEndTime(),monitorProcessItem.getStartTime()));
        }
        return monitorProcessMapper.updateMonitorProcess(monitorProcessItem);
    }

    /**
     * 查询某一个流程对应的子流程
     * @param monitorNodeItem
     * @return
     */


    @Override
    public int updateMonitorProcessByIds(MonitorProcessItem monitorProcessItem) {//暂不考虑这个
        return 0;
    }

    @Override
    public int countMonitorProcess(MonitorProcessItem monitorProcessItem) {
        return monitorProcessMapper.countMonitorProcess(monitorProcessItem);
    }


}

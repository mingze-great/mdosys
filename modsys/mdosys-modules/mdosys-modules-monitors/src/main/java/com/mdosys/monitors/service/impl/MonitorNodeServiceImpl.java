package com.mdosys.monitors.service.impl;

import com.mdosys.monitors.domain.MonitorNodeItem;
import com.mdosys.monitors.mapper.MonitorNodeMapper;
import com.mdosys.monitors.service.IMonitorNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorNodeServiceImpl implements IMonitorNodeService {
    @Autowired
    private MonitorNodeMapper monitorNodeMapper;

    @Override
    public List<MonitorNodeItem> selectMonitorNodeList(MonitorNodeItem monitorNodeItem) {
        return monitorNodeMapper.selectMonitorNodeList(monitorNodeItem);
    }
}

package com.mdosys.monitors.service;

import com.mdosys.monitors.domain.MonitorNodeItem;

import java.util.List;

public interface IMonitorNodeService {
    public List<MonitorNodeItem> selectMonitorNodeList(MonitorNodeItem monitorNodeItem);

}

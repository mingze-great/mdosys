package com.mdosys.monitors.mapper;

import com.mdosys.monitors.domain.MonitorNodeItem;

import java.util.List;

public interface MonitorNodeMapper {
    public List<MonitorNodeItem> selectMonitorNodeList(MonitorNodeItem monitorNodeItem);
}

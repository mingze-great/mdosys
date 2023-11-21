package com.mdosys.monitors.controller;

import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.common.core.web.page.TableDataInfo;

import com.mdosys.monitors.domain.MonitorNodeItem;
import com.mdosys.monitors.domain.MonitorProcessItem;
import com.mdosys.monitors.service.IMonitorNodeService;
import com.mdosys.monitors.service.IMonitorProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 流程监控Controller
 */
@RestController
@RequestMapping("/project/monitor")
public class MonitorController extends BaseController {
    @Autowired
    private IMonitorProcessService monitorProcessService;

    @Autowired
    private IMonitorNodeService monitorNodeService;


    /**
     * 获取流程列表
     * @param monitorProcessItem
     * @return 流程列表
     */

    @GetMapping("/list")
    public AjaxResult list(MonitorProcessItem monitorProcessItem){
        startPage();
        System.out.println(monitorProcessItem.toString());
        List<MonitorProcessItem> list = monitorProcessService.selectMonitorProcessList(monitorProcessItem);
        System.out.println("只有list"+list);
        return AjaxResult.success(list).put("total",getDataTable(list).getTotal());
    }


    @GetMapping("/nodeList")
    public AjaxResult nodeList(MonitorNodeItem monitorNodeItem){
        System.out.println(monitorNodeItem.toString());
        List<MonitorNodeItem> list = monitorNodeService.selectMonitorNodeList(monitorNodeItem);
        System.out.println("子节点查询成功："+list);
        return AjaxResult.success(list);
    }

    @GetMapping("/idItem")
    public AjaxResult idItem(MonitorProcessItem monitorProcessItem){
        return AjaxResult.success(monitorProcessService.selectMonitorProgressById(monitorProcessItem));
    }
    @PatchMapping("/update")
    public AjaxResult edit(@RequestBody MonitorProcessItem monitorProcessItem) {
//        monitorProcessItem.setRunFlag();
        if(monitorProcessItem.isRunFlag()){//开始运行
            monitorProcessItem.setStartTime(DateUtils.getNowDate());
        }
        else{//终止运行
            monitorProcessItem.setEndTime(DateUtils.getNowDate());
            System.out.println("现在是开始时间！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！"+monitorProcessItem.getStartTime());
            monitorProcessItem.setRunTime(DateUtils.getDatePoor(monitorProcessItem.getStartTime(),monitorProcessItem.getEndTime()));
            System.out.println("终止时间为："+monitorProcessItem.getEndTime());
            System.out.println("开始运行时间为："+monitorProcessItem.getStartTime());
            System.out.println("总运行时间为："+DateUtils.getDatePoor(monitorProcessItem.getEndTime(),monitorProcessItem.getStartTime()));
            System.out.println("总运行时间为："+monitorProcessItem.getRunTime());

        }
        System.out.println("现在需要更新的流程是："+monitorProcessItem);
        return toAjax(monitorProcessService.updateMonitorProcess(monitorProcessItem));
    }}


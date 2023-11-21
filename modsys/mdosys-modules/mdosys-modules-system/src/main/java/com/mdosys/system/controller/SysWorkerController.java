package com.mdosys.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import com.mdosys.common.log.annotation.Log;
//import com.mdosys.common.log.enums.BusinessType;
import com.mdosys.common.security.annotation.RequiresPermissions;
import com.mdosys.system.domain.SysWorker;
import com.mdosys.system.service.ISysWorkerService;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.common.core.utils.poi.ExcelUtil;
import com.mdosys.common.core.web.page.TableDataInfo;

/**
 * worker分组Controller
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
@RestController
@RequestMapping("/worker")
public class SysWorkerController extends BaseController
{
    @Autowired
    private ISysWorkerService sysWorkerService;

    /**
     * 查询worker分组列表
     */
//    @RequiresPermissions("system:worker:list")
    @GetMapping("/list")
    public TableDataInfo list(SysWorker sysWorker)
    {
        startPage();
        List<SysWorker> list = sysWorkerService.selectSysWorkerList(sysWorker);
        return getDataTable(list);
    }

    /**
     * 导出worker分组列表
     */
//    @RequiresPermissions("system:worker:export")
//    @Log(title = "worker分组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysWorker sysWorker)
    {
        List<SysWorker> list = sysWorkerService.selectSysWorkerList(sysWorker);
        ExcelUtil<SysWorker> util = new ExcelUtil<SysWorker>(SysWorker.class);
        util.exportExcel(response, list, "worker分组数据");
    }

    /**
     * 获取worker分组详细信息
     */
//    @RequiresPermissions("system:worker:query")
    @GetMapping(value = "/detail/{workerId}")
    public AjaxResult getInfo(@PathVariable("workerId") Long workerId)
    {
        return AjaxResult.success(sysWorkerService.selectSysWorkerByWorkerId(workerId));
    }

    /**
     * 新增worker分组
     */
//    @RequiresPermissions("system:worker:add")
//    @Log(title = "worker分组", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysWorker sysWorker)
    {
        return toAjax(sysWorkerService.insertSysWorker(sysWorker));
    }

    /**
     * 修改worker分组
     */
//    @RequiresPermissions("system:worker:edit")
//    @Log(title = "worker分组", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody SysWorker sysWorker)
    {
        return toAjax(sysWorkerService.updateSysWorker(sysWorker));
    }

    /**
     * 删除worker分组
     */
//    @RequiresPermissions("system:worker:remove")
//    @Log(title = "worker分组", businessType = BusinessType.DELETE)
	@DeleteMapping("/remove/{workerIds}")
    public AjaxResult remove(@PathVariable Long[] workerIds)
    {
        return toAjax(sysWorkerService.deleteSysWorkerByWorkerIds(workerIds));
    }
}

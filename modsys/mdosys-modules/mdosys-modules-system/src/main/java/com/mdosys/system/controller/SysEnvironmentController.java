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
import com.mdosys.system.domain.SysEnvironment;
import com.mdosys.system.service.ISysEnvironmentService;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.common.core.utils.poi.ExcelUtil;
import com.mdosys.common.core.web.page.TableDataInfo;

/**
 * 环境Controller
 *
 * @author bairuizhe
 * @date 2022-11-07
 */
@RestController
@RequestMapping("/environment")
public class SysEnvironmentController extends BaseController
{
    @Autowired
    private ISysEnvironmentService sysEnvironmentService;

    /**
     * 查询环境列表
     */
//    @RequiresPermissions("system:environment:list")
    @GetMapping("/list")
    public TableDataInfo list(SysEnvironment sysEnvironment)
    {
        startPage();
        List<SysEnvironment> list = sysEnvironmentService.selectSysEnvironmentList(sysEnvironment);
        return getDataTable(list);
    }

    /**
     * 导出环境列表
     */
//    @RequiresPermissions("system:environment:export")
//    @Log(title = "环境", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysEnvironment sysEnvironment)
    {
        List<SysEnvironment> list = sysEnvironmentService.selectSysEnvironmentList(sysEnvironment);
        ExcelUtil<SysEnvironment> util = new ExcelUtil<SysEnvironment>(SysEnvironment.class);
        util.exportExcel(response, list, "环境数据");
    }

    /**
     * 获取环境详细信息
     */
//    @RequiresPermissions("system:environment:query")
    @GetMapping(value = "/{envirId}")
    public AjaxResult getInfo(@PathVariable("envirId") Long envirId)
    {
        return AjaxResult.success(sysEnvironmentService.selectSysEnvironmentByEnvirId(envirId));
    }

    /**
     * 新增环境
     */
//    @RequiresPermissions("system:environment:add")
//    @Log(title = "环境", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SysEnvironment sysEnvironment)
    {
        return toAjax(sysEnvironmentService.insertSysEnvironment(sysEnvironment));
    }

    /**
     * 修改环境
     */
//    @RequiresPermissions("system:environment:edit")
//    @Log(title = "环境", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody SysEnvironment sysEnvironment)
    {
        return toAjax(sysEnvironmentService.updateSysEnvironment(sysEnvironment));
    }

    /**
     * 删除环境
     */
//    @RequiresPermissions("system:environment:remove")
//    @Log(title = "环境", businessType = BusinessType.DELETE)
	@DeleteMapping("/remove/{envirIds}")
    public AjaxResult remove(@PathVariable Long[] envirIds)
    {
        return toAjax(sysEnvironmentService.deleteSysEnvironmentByEnvirIds(envirIds));
    }
}

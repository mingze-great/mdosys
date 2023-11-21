package com.mdosys.knowledgelib.controller;

import com.mdosys.common.core.utils.poi.ExcelUtil;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
//import com.mdosys.common.log.annotation.Log;
//import com.mdosys.common.log.enums.BusinessType;
import com.mdosys.common.security.utils.SecurityUtils;
import com.mdosys.knowledgelib.domain.CompType;
import com.mdosys.knowledgelib.service.ICompTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组件类型Controller
 *
 * @author brz
 * @date 2022-09-02
 */
@RestController
@RequestMapping("/comptype")
public class CompTypeController extends BaseController {
    @Autowired
    private ICompTypeService compTypeService;

    /**
     * 查询组件类型列表
     */
//    @RequiresPermissions("system:comptype:list")
    @GetMapping("/list")
    public AjaxResult list(CompType compType)
    {
        List<CompType> list = compTypeService.selectCompTypeList(compType);
        return AjaxResult.success(list);
    }

    /**
     * 导出组件类型列表
     */
//    @RequiresPermissions("system:comptype:export")
//    @Log(title = "组件类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CompType compType)
    {
        List<CompType> list = compTypeService.selectCompTypeList(compType);
        ExcelUtil<CompType> util = new ExcelUtil<CompType>(CompType.class);
        util.exportExcel(response, list, "组件类型数据");
    }

    /**
     * 获取组件类型详细信息
     */
//    @RequiresPermissions("system:comptype:query")
    @GetMapping(value = "/detail/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") Long typeId)
    {
        return AjaxResult.success(compTypeService.selectCompTypeByTypeId(typeId));
    }

    /**
     * 新增组件类型
     */
//    @RequiresPermissions("system:comptype:add")
//    @Log(title = "组件类型", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody CompType compType)
    {
        compType.setCreateBy(SecurityUtils.getUsername());
        return toAjax(compTypeService.insertCompType(compType));
    }

    /**
     * 修改组件类型
     */
//    @RequiresPermissions("system:comptype:edit")
//    @Log(title = "组件类型", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/update")
    public AjaxResult edit(@RequestBody CompType compType)
    {
        return toAjax(compTypeService.updateCompType(compType));
    }

    /**
     * 删除组件类型
     */
//    @RequiresPermissions("system:comptype:remove")
//    @Log(title = "组件类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/del/{typeIds}")
    public AjaxResult remove(@PathVariable Long[] typeIds)
    {
        return toAjax(compTypeService.deleteCompTypeByTypeIds(typeIds));
    }
}

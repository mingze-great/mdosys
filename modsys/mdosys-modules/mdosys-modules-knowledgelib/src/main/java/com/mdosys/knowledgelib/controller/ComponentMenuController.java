package com.mdosys.knowledgelib.controller;

import com.mdosys.common.core.utils.poi.ExcelUtil;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
//import com.mdosys.common.log.annotation.Log;
//import com.mdosys.common.log.enums.BusinessType;
import com.mdosys.knowledgelib.domain.ComponentMenu;
import com.mdosys.knowledgelib.service.IComponentMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * component_menuController
 *
 * @author hwj
 * @date 2022-09-19
 */
@RestController
@RequestMapping("/compMenu")
public class ComponentMenuController extends BaseController
{
    @Autowired
    private IComponentMenuService componentMenuService;

    /**
     * 查询component_menu列表
     */
//    @RequiresPermissions("knowledgelib:menu:list")
    @GetMapping("/list")
    public AjaxResult list(ComponentMenu componentMenu) {
        List<ComponentMenu> list = componentMenuService.selectComponentMenuList(componentMenu);
        return AjaxResult.success(list);
    }

    /**
     * 查询用户的个人组件
     * @param create_by 创建人的id
     * @return
     */
    @GetMapping("/listByUser/{id}")
    public AjaxResult listByUser(@PathVariable("id") Long create_by) {
        List<ComponentMenu> list = componentMenuService.selectMenuListByUserId(create_by);
        return AjaxResult.success(list);
    }

    /**
     * 查询公共组件
     * @return
     */
    @GetMapping("/pubList")
    public AjaxResult pubList() {
        List<ComponentMenu> list = componentMenuService.selectPubMenuList();
        return AjaxResult.success(list);
    }

    /**
     * 查询菜单列表（不包括组件）
     * @return
     */
    @GetMapping("/treeList")
    public AjaxResult treeList() {
        List<ComponentMenu> list = componentMenuService.selectTreeMenuList();
        return AjaxResult.success(list);
    }

    /**
     * 导出component_menu列表
     */
//    @RequiresPermissions("knowledgelib:menu:export")
//    @Log(title = "component_menu", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ComponentMenu componentMenu)
    {
        List<ComponentMenu> list = componentMenuService.selectComponentMenuList(componentMenu);
        ExcelUtil<ComponentMenu> util = new ExcelUtil<ComponentMenu>(ComponentMenu.class);
        util.exportExcel(response, list, "component_menu数据");
    }

    /**
     * 获取component_menu详细信息
     */
//    @RequiresPermissions("knowledgelib:menu:query")
    @GetMapping(value = "/detail/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(componentMenuService.selectComponentMenuById(id));
    }

    /**
     * 新增component_menu
     */
//    @RequiresPermissions("knowledgelib:menu:add")
//    @Log(title = "component_menu", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody ComponentMenu componentMenu)
    {
        return toAjax(componentMenuService.insertComponentMenu(componentMenu));
    }

    /**
     * 修改component_menu
     */
//    @RequiresPermissions("knowledgelib:menu:edit")
//    @Log(title = "component_menu", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/update")
    public AjaxResult edit(@RequestBody ComponentMenu componentMenu)
    {
        return toAjax(componentMenuService.updateComponentMenu(componentMenu));
    }

    /**
     * 删除component_menu
     */
//    @RequiresPermissions("knowledgelib:menu:remove")
//    @Log(title = "component_menu", businessType = BusinessType.DELETE)
	@DeleteMapping("/del/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(componentMenuService.deleteComponentMenuByIds(ids));
    }
}

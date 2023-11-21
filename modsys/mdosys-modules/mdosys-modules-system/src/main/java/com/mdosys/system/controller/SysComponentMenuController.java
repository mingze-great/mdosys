package com.mdosys.system.controller;

import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.system.domain.SysComponent;
import com.mdosys.system.domain.SysComponentMenu;
import com.mdosys.system.service.ISysComponentMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/componentMenu")
public class SysComponentMenuController extends BaseController {

    @Autowired
    private ISysComponentMenuService componentMenuService;

    @GetMapping("/list")
    public AjaxResult list(SysComponentMenu sysComponentMenu) {
        List<SysComponentMenu> list = componentMenuService.selectSysComponentMenuList(sysComponentMenu);
        return AjaxResult.success(list);
    }

    /**
     * 新增组件菜单
     */
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody SysComponentMenu sysComponentMenu) {
        sysComponentMenu.setUserId(1L);
        return toAjax(componentMenuService.insertSysComponentMenu(sysComponentMenu));
    }

    /**
     * 新增组件菜单
     */
    @PutMapping(value = "/update")
    public AjaxResult update(@RequestBody SysComponentMenu sysComponentMenu) {
        return toAjax(componentMenuService.updateSysComponentMenu(sysComponentMenu));
    }

    /**
     * 删除组件菜单
     */
    @GetMapping(value = "/del/{id}")
    public AjaxResult updateCompMenu(@PathVariable(value = "id") Long id) {
        return toAjax(componentMenuService.deleteSysComponentMenuById(id));
    }
}

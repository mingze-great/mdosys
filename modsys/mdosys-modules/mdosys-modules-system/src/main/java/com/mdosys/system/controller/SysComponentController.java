package com.mdosys.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.mdosys.common.core.constant.SecurityConstants;
import com.mdosys.common.core.utils.uuid.CodeUtils;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.common.core.web.page.TableDataInfo;
import com.mdosys.common.security.annotation.RequiresPermissions;
import com.mdosys.common.security.utils.SecurityUtils;
import com.mdosys.system.api.domain.SysUser;
import com.mdosys.system.domain.Enum.ComponentTypeEnum;
import com.mdosys.system.domain.SysComponent;
import com.mdosys.system.domain.SysComponentFile;
import com.mdosys.system.domain.Vo.ComponentInfo;
import com.mdosys.system.service.ISysComponentFileService;
import com.mdosys.system.service.ISysComponentService;
import com.mdosys.system.service.ISysParamInfoService;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 组件Controller
 * 
 * @author hwj
 * @date 2022-10-25
 */
@RestController
@RequestMapping("/component")
public class SysComponentController extends BaseController {
    @Autowired
    private ISysComponentService sysComponentService;

    @Autowired
    private ISysComponentFileService sysComponentFileService;

    @Autowired
    private ISysParamInfoService paramInfoService;

    /**
     * 查询组件列表
     */
//    @RequiresPermissions("system:component:list")
    @GetMapping("/list")
    public AjaxResult list(SysComponent sysComponent) {
        List<SysComponent> list = sysComponentService.selectSysComponentList(sysComponent);
        return AjaxResult.success(list);
    }

    /**
     * 查询公共组件列表
     */
//    @RequiresPermissions("system:component:list")
    @GetMapping("/pubList")
    public AjaxResult pubList() {
        List<SysComponent> list = sysComponentService.selectPubSysComponentList();
        return AjaxResult.success(list);
    }

    /**
     * 查询组件菜单列表
     */
//    @RequiresPermissions("system:component:list")
    @GetMapping("/menuList")
    public AjaxResult menuList() {
        List<SysComponent> list = sysComponentService.selectSysComponentMenuList();
        return AjaxResult.success(list);
    }


    /**
     * 新增组件
     */
//    @RequiresPermissions("system:component:add")
//    @Log(title = "组件", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody SysComponent sysComponent) throws CodeUtils.CodeGenerateException {
        sysComponent.setUserId(1L);
        try {
            return AjaxResult.success(sysComponentService.insertSysComponent(sysComponent));
        }catch (CodeUtils.CodeGenerateException e){
            return AjaxResult.error("生成code异常");
        }
    }

    /**
     * 修改组件
     */
//    @RequiresPermissions("system:component:edit")
//    @Log(title = "组件", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody SysComponent sysComponent) {
        return toAjax(sysComponentService.updateSysComponent(sysComponent));
    }

    /**
     * 取消授权
     * @param id
     * @return
     */
    @GetMapping(value = "/revoke/{id}")
    public AjaxResult revokeSysComponent(@PathVariable Long id){
        return toAjax(sysComponentService.revokeSysComponent(id));
    }

    /**
     * 删除组件
     */
//    @RequiresPermissions("system:component:remove")
//    @Log(title = "组件", businessType = BusinessType.DELETE)
	@DeleteMapping("/del/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(sysComponentService.deleteSysComponentById(id));
    }

    /**
     * 获取组件参数
     */
    @GetMapping("/params/{id}")
    public AjaxResult getParams(@PathVariable Long id) {
        SysComponentFile sysComponentFile = new SysComponentFile();
        sysComponentFile.setCompId(id);
        sysComponentFile.setType("输入文件");
        return AjaxResult.success(paramInfoService.selectSysParamList(sysComponentFile));
    }

    /**
     * 根据组件id查询组件菜单列表
     */
    @GetMapping("/info/{id}")
    public AjaxResult getComponentInfo(@PathVariable Long id) {
        SysComponent component = sysComponentService.selectSysComponentById(id);
        if (component == null)  return AjaxResult.error("组件不存在！");
        SysComponentFile componentFile = sysComponentFileService.selectSysComponentFilePath(id);
        ComponentInfo info = new ComponentInfo(component);
        info.setExecutePath(componentFile.getPath());
        return AjaxResult.success(info);
    }
}

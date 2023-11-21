package com.mdosys.knowledgelib.controller;

import com.mdosys.common.core.utils.poi.ExcelUtil;
import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.common.core.web.page.TableDataInfo;
//import com.mdosys.common.log.annotation.Log;
//import com.mdosys.common.log.enums.BusinessType;
import com.mdosys.common.security.utils.SecurityUtils;
import com.mdosys.knowledgelib.domain.CompFile;
import com.mdosys.knowledgelib.domain.Component;
import com.mdosys.knowledgelib.service.IComponentService;
import com.mdosys.system.api.RemoteFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组件Controller
 *
 * @author brz
 * @date 2022-09-01
 */
@RestController
@RequestMapping("/component")
public class ComponentController extends BaseController {
    @Autowired
    private IComponentService ComponentService;

    @Autowired
    private RemoteFileService remoteFileService;

//    /**
//     * 查询组件列表
//     */
////    @RequiresPermissions("knowledgelib:component:list")
//    @GetMapping("/list")
//    public TableDataInfo list(Component component) {
//        startPage();
//        List<Component> list = ComponentService.selectComponentList(component);
//        return getDataTable(list);
//    }

    /**
     * 公共组件列表
     */
    @GetMapping(value = "/publist")
    public TableDataInfo pList(Component component) {
        startPage();
        List<Component> list = ComponentService.selectPubComp(component);
        return getDataTable(list);
    }

    /**
     * 我的组件列表
     */
    @GetMapping(value = "/mylist/{userId}")
    public TableDataInfo myList(@PathVariable("userId") Long userId) {
        startPage();
        List<Component> list = ComponentService.selectMyCompByUserId(userId);
        return getDataTable(list);
    }

//    /**
//     * 按类型查询组件列表
//     */
////    @RequiresPermissions("knowledgelib:component:list")
//    @GetMapping(value = "/type_id/{typeId}")
//    public TableDataInfo list(@PathVariable("typeId") Long typeId) {
//        startPage();
//        List<Component> list = ComponentService.selectComponentByTypeId(typeId);
//        return getDataTable(list);
//    }

    /**
     * 导出组件列表
     */
//    @RequiresPermissions("knowledgelib:component:export")
//    @Log(title = "组件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Component component) {
        List<Component> list = ComponentService.selectComponentList(component);
        ExcelUtil<Component> util = new ExcelUtil<Component>(Component.class);
        util.exportExcel(response, list, "组件数据");
    }

    /**
     * 获取组件详细信息
     */
//    @RequiresPermissions("knowledgelib:component:query")
    @GetMapping(value = "/detail/{compId}")
    public AjaxResult getInfo(@PathVariable("compId") Long compId) {
        return AjaxResult.success(ComponentService.selectComponentByCompId(compId));
    }

    /**
     * 获取组件文件列表
     */
    @GetMapping(value = "/compfile/{compId}")
    public List<CompFile> getCompFile(@PathVariable("compId") Long compId) {
        return ComponentService.selectCompFileByCompId(compId);
    }

    /**
     * 新增组件
     */
//    @RequiresPermissions("knowledgelib:component:add")
//    @Log(title = "组件", businessType = BusinessType.INSERT)
    @PostMapping(value = "add")
    public AjaxResult add(Component component, CompFile compFile, @RequestPart(value = "files") List<MultipartFile> files) {
        try {
//            remoteFileService.uploadComponent(files, component.getCompName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        component.setUserId(SecurityUtils.getUserId());
        AjaxResult ajaxResult = toAjax(ComponentService.insertComponent(component));
        Long lastInsertId = ComponentService.selectLastInsertId();
        for (int i=0; i<files.size(); i++) {
            compFile.setCompId(lastInsertId);
            compFile.setFileName(files.get(i).getOriginalFilename());
            compFile.setFilePath("/components/" + component.getCompName() + "/" + files.get(i).getOriginalFilename());
            ComponentService.insertCompFile(compFile);
        }
        return ajaxResult;
    }

    /**
     * 修改组件
     */
//    @RequiresPermissions("knowledgelib:component:edit")
//    @Log(title = "组件", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/edit")
    public AjaxResult edit(Component component, CompFile compFile, @RequestPart(value = "files") List<MultipartFile> files) {
        try {
//            remoteFileService.changeComponent(files, ComponentService.selectComponentByCompId(component.getCompId()).getCompName(), component.getCompName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ComponentService.deleteCompFileByCompId(component.getCompId());
        for (int i=0; i<files.size(); i++) {
            compFile.setCompId(component.getCompId());
            compFile.setFileName(files.get(i).getOriginalFilename());
            compFile.setFilePath("/components/" + component.getCompName() + "/" + files.get(i).getOriginalFilename());
            ComponentService.insertCompFile(compFile);
        }
        return toAjax(ComponentService.updateComponent(component));
    }

    /**
     * 删除组件
     */
//    @RequiresPermissions("knowledgelib:component:remove")
//    @Log(title = "组件", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove/{compIds}")
    public AjaxResult remove(@PathVariable Long[] compIds) {
        for (int i = 0; i < compIds.length; i++) {
            ComponentService.deleteCompFileByCompId(compIds[i]);
            Component component = ComponentService.selectComponentByCompId(compIds[i]);
            try {
//                remoteFileService.deleteComponent(component.getCompName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toAjax(ComponentService.deleteComponentByCompIds(compIds));
    }
}

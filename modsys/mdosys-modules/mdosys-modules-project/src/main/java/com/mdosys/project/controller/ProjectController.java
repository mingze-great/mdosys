package com.mdosys.project.controller;

import com.mdosys.common.core.web.controller.BaseController;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.project.service.IProjectService;
import com.mdosys.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {


    private IProjectService projectService;

    @Autowired
    public void setProjectService(IProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * @param projectName 项目名称
     * @param description 项目描述
     * @return 创建项目是否完成的相关信息 id 是否成功
     */
    @PostMapping()
    public AjaxResult createProject(@RequestParam("projectName") String projectName,
                                    @RequestParam(value = "description", required = false) String description) {
        AjaxResult result;
        System.out.println("create project");
        if (checkStrContent(projectName, 100) || checkStrContent(description, 200)) {
            result = AjaxResult.error();
            return result;
        }
//        LoginUser loginUser= SecurityUtils.getLoginUser();
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(15L);
        loginUser.setUsername("zhangkang");
        return projectService.createProject(projectName, description, loginUser);
    }


    /**
     * @param code        更新项目的代号
     * @param projectName 项目名称
     * @param description 项目描述
     * @return 创建项目是否完成的相关信息 id 是否成功
     */
    @PutMapping("/{code}")
    public AjaxResult updateProject(@PathVariable("code") Long code,
                                    @RequestParam("projectName") String projectName,
                                    @RequestParam(value = "description", required = false) String description) {
//        LoginUser loginUser = SecurityUtils.getLoginUser();
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(15L);
        loginUser.setUsername("zhangkang");
        return projectService.updateProject(code, projectName, description, loginUser);
    }

    @GetMapping("/queryByCode")
    public AjaxResult queryProjectByCode(@RequestParam("code") Long code) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(15L);
        loginUser.setUsername("zhangkang");
        return projectService.selectProjectByCode(code, loginUser);
    }

    @GetMapping("/queryByName")
    public AjaxResult queryProjectByName(@RequestParam("projectName") String projectName) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(15L);
        loginUser.setUsername("zhangkang");
        if (checkStrContent(projectName, 100)) {
            return AjaxResult.error();
        }
        return projectService.queryProjectByNamePaging(projectName, loginUser);
    }

    @GetMapping("/user-projects")
    public AjaxResult queryProjectByUser(@RequestParam(value = "userId",required = false) Long userId) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(15L);
        loginUser.setUsername("zhangkang");
        if (userId==null){
            userId=loginUser.getUserid();
        }
        return projectService.queryUserAllProjectPaging(userId, loginUser);
    }


    @DeleteMapping(value = "/{code}")
    public AjaxResult delProjectByCode(@PathVariable("code") long projectCode, @RequestParam("forceDel") boolean forceDel) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(15L);
        loginUser.setUsername("zhangkang");
        if (forceDel) {
            return projectService.forceDelProjectByCode(projectCode, loginUser);
        }
        return projectService.delProjectByCode(projectCode, loginUser);
    }

}

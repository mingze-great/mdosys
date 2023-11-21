package com.mdosys.project.service.impl;

import com.github.pagehelper.PageInfo;
import com.mdosys.common.core.constant.Constants;
import com.mdosys.common.core.enums.Status;
import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.common.core.utils.PageUtils;
import com.mdosys.common.core.utils.uuid.CodeUtils;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.project.domain.Project;
import com.mdosys.project.domain.ProjectUserRelation;
import com.mdosys.project.mapper.ProjectMapper;
import com.mdosys.project.mapper.ProjectUserRelationMapper;
import com.mdosys.project.service.IProjectService;
import com.mdosys.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService {

    private ProjectMapper projectMapper;

    private ProjectUserRelationMapper projectUserRelationMapper;

    @Autowired
    private void setProjectMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Autowired
    private void setProjectUserRelationMapper(ProjectUserRelationMapper projectUserRelationMapper) {
        this.projectUserRelationMapper = projectUserRelationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult createProject(String name, String description, LoginUser loginUser) {
        AjaxResult result;
        Project project = projectMapper.selectProjectByName(name, loginUser.getUserid());
        if (project != null) {
            result = AjaxResult.error(Status.PROJECT_ALREADY_EXISTS, name);
            return result;
        }
        try {
            Date now = new Date();
            project = Project.newBuilder()
                    .code(CodeUtils.getInstance().genCode())
                    .name(name)
                    .description(description)
                    .updateTime(DateUtils.parseDate(now))
                    .createTime(DateUtils.parseDate(now))
                    .perm(1)
                    .userId(loginUser.getUserid())
                    .userName(loginUser.getUsername())
                    .build();
        } catch (CodeUtils.CodeGenerateException e) {
            result = AjaxResult.error(Status.CODE_GENERATE_ERROR, name);
            return result;
        }
        int a = projectMapper.insertProjectByCode(project);
        ProjectUserRelation projectUserRelation = new ProjectUserRelation();
        projectUserRelation.setProjectId(project.getId());
        projectUserRelation.setUserId(project.getUserId());
        projectUserRelation.setPermission(Constants.ALL_PERMISSIONS);
        int b = projectUserRelationMapper.insertProjectUserRelation(projectUserRelation);
        if (a < 0 || b < 0) {
            return AjaxResult.error(Status.CREATE_PROJECT_ERROR);
        } else {
            result = AjaxResult.success(project);
            return result;
        }
    }

    @Override
    public AjaxResult delProjectByCode(Long projectCode, LoginUser loginUser) {
        AjaxResult result = getAuthorizedProjectAjaxResult(projectCode, loginUser, Constants.ALL_PERMISSIONS);
        if (result.isError()) return result;
        int a = projectMapper.delProjectByCode(projectCode);
        if (a < 0) {
            result = AjaxResult.error(Status.ERROR);
            return result;
        } else {
            return AjaxResult.success(projectCode);
        }
    }

    /**
     * 强制删除项目 当项目中还有process存在时不允许操作
     *
     * @param projectCode 要删除的项目code
     * @param loginUser   当前登陆用户
     * @return 是否标志成功
     */
    @Override
    public AjaxResult forceDelProjectByCode(Long projectCode, LoginUser loginUser) {
        return AjaxResult.error(Status.NOT_IMPLEMENTED, "强制删除无法使用，请将forceDel置为false");
    }

    /**
     * 根据project code更新项目 名称 描述 等相关信息
     *
     * @param projectCode 项目code
     * @param name        要修改的项目名称
     * @param description 要修改的项目描述
     * @param loginUser   当前登陆用户
     * @return AjaxResult
     */
    @Override
    public AjaxResult updateProject(long projectCode, String name, String description, LoginUser loginUser) {
        //判断当前用户是否对此项目有更新权限
        AjaxResult result = getAuthorizedProjectAjaxResult(projectCode, loginUser, Constants.ALL_PERMISSIONS);
        if (result.isError()) return result;
        // 项目名是否存在
        Project project = projectMapper.selectProjectByName(name, loginUser.getUserid());
        if (project != null) {
            result = AjaxResult.error(Status.PROJECT_ALREADY_EXISTS, name);
            return result;
        }
        Date now = new Date();
        project = Project.newBuilder()
                .code(projectCode)
                .name(name)
                .description(description)
                .updateTime(DateUtils.parseDate(now))
                .build();
        projectMapper.updateProjectByCode(project);
        return AjaxResult.success(project);
    }


    /**
     * 根据项目code返回项目详细信息
     *
     * @param projectCode 项目code
     * @param loginUser   当前登陆用户
     * @return 带有project信息的AjaxResult
     */
    @Override
    public AjaxResult selectProjectByCode(long projectCode, LoginUser loginUser) {
        return getAuthorizedProjectAjaxResult(projectCode,
                loginUser,
                Constants.READ_PERMISSION);

        //todo 把project 中详细信息查出来
    }

    /**
     * 根据项目名称进行模糊查询
     *
     * @param projectName 查询字段
     * @param loginUser   当前用户
     * @return 查到的结果列表
     */
    @Override
    public AjaxResult queryProjectByNamePaging(String projectName, LoginUser loginUser) {
        PageUtils.startPage();
        List<Project> projectList = projectMapper.queryProjectByName(projectName, loginUser.getUserid());
        PageInfo<Project> pageInfo = new PageInfo<>(projectList);
        AjaxResult ajaxResult;
        if (pageInfo.getSize() == 0) {
            return AjaxResult.success(Status.SUCCESS_NO_CONTENT);
        }
        ajaxResult = AjaxResult.success(pageInfo);
        return ajaxResult;
    }

    /**
     * 查询出某一个用户id下的所有项目，包括自己创建的和自己参与的
     *
     * @param userId    用户id
     * @param loginUser 当前登陆用户
     * @return 查到的用户列表
     */
    @Override
    public AjaxResult queryUserAllProjectPaging(long userId, LoginUser loginUser) {
        if (userId == loginUser.getUserid()) {
            PageUtils.startPage();
            List<Project> projectList = projectMapper.queryProjectByUserId(userId);
            PageInfo<Project> pageInfo = new PageInfo<>(projectList);
            return AjaxResult.success(pageInfo);
        } else {
            return AjaxResult.error(Status.UNAUTHORIZED);
        }
    }

    /**
     * 根据项目的code检查项目是否存在 并检查响应权限是否满足
     *
     * @param projectCode 目标项目code
     * @param loginUser   当前登陆用户
     * @param permission  要检查的权限
     * @return AjaxResult.error 或者返回null表示检查通过
     */
    private AjaxResult getAuthorizedProjectAjaxResult(long projectCode, LoginUser loginUser, int permission) {
        AjaxResult result;
        Project project = projectMapper.selectProjectByCode(projectCode);
        if (project == null) {
            result = AjaxResult.error(Status.PROJECT_NOT_FOUNT, projectCode);
            return result;
        }
        if (queryPermission(loginUser, project.getId()) < permission) {
            result = AjaxResult.error(Status.PROJECT_UNAUTHORIZED_ERROR);
            return result;
        }
        result = AjaxResult.success(project);
        return result;
    }


    private int queryPermission(LoginUser loginUser, Long projectId) {
        // todo 正式启动这里打开
        //  if (loginUser.getSysUser().isAdmin()) {
        //            return Constants.READ_PERMISSION;
        //        }

        if (projectId.longValue() == loginUser.getUserid().longValue()) {
            return Constants.ALL_PERMISSIONS;
        }

        ProjectUserRelation projectUser = projectUserRelationMapper.selectProjectRelation(projectId, loginUser.getUserid());

        if (projectUser == null) {
            return 0;
        }
        return projectUser.getPermission();
    }
}

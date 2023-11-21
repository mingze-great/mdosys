package com.mdosys.project.service;

import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.system.api.model.LoginUser;

public interface IProjectService {

    AjaxResult createProject(String name, String description, LoginUser loginUser);

    /**
     * 根据project code更新项目 名称 描述 等相关信息
     *
     * @param projectCode 项目code
     * @param name        要修改的项目名称
     * @param description 要修改的项目描述
     * @param loginUser   当前登陆用户
     * @return AjaxResult
     */
    AjaxResult updateProject(long projectCode, String name, String description, LoginUser loginUser);

    /**
     * 根据项目code 删除项目 （将项目删除标志设置为0 -- 0 not available, 1 available）
     *
     * @param projectCode 要删除的项目code
     * @param loginUser 当前登陆用户
     * @return 是否标志成功
     */
    AjaxResult delProjectByCode(Long projectCode, LoginUser loginUser) ;

    /**
     * 强制删除项目 当项目中还有process存在时不允许操作
     *
     * @param projectCode 要删除的项目code
     * @param loginUser 当前登陆用户
     * @return 是否标志成功
     */
    AjaxResult forceDelProjectByCode(Long projectCode, LoginUser loginUser);

    /**
     * 根据项目id返回项目详细信息
     *
     * @param projectCode 项目code
     * @param loginUser   当前登陆用户
     * @return 带有project信息的AjaxResult
     */
    AjaxResult selectProjectByCode(long projectCode, LoginUser loginUser);


    /**
     * 根据项目名称进行模糊查询
     *
     * @param projectName 查询字段
     * @param loginUser 当前用户
     * @return 查到的结果列表
     */
    AjaxResult queryProjectByNamePaging(String projectName, LoginUser loginUser);

    AjaxResult queryUserAllProjectPaging(long userId, LoginUser loginUser);
}

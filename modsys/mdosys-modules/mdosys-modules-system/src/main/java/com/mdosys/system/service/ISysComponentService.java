package com.mdosys.system.service;

import com.mdosys.common.core.utils.uuid.CodeUtils;
import com.mdosys.system.domain.SysComponent;

import java.util.List;

/**
 * 组件Service接口
 * 
 * @author hwj
 * @date 2022-10-25
 */
public interface ISysComponentService 
{
    /**
     * 查询组件
     * 
     * @param id 组件主键
     * @return 组件
     */
    public SysComponent selectSysComponentById(Long id);

    /**
     * 查询组件列表
     * 
     * @param sysComponent 组件
     * @return 组件集合
     */
    public List<SysComponent> selectSysComponentList(SysComponent sysComponent);

    /**
     * 查询公共组件列表
     * @return 组件集合
     */
    public List<SysComponent> selectPubSysComponentList();

    /**
     * 查询组件菜单列表
     *
     * @return 组件集合
     */
    public List<SysComponent> selectSysComponentMenuList();


    /**
     * 新增组件
     * 
     * @param sysComponent 组件
     * @return 结果
     */
    public Long insertSysComponent(SysComponent sysComponent) throws CodeUtils.CodeGenerateException;

    /**
     * 修改组件
     * 
     * @param sysComponent 组件
     * @return 结果
     */
    public int updateSysComponent(SysComponent sysComponent);

    /**
     * 批量删除组件
     * 
     * @param ids 需要删除的组件主键集合
     * @return 结果
     */
    public int deleteSysComponentByIds(Long[] ids);

    /**
     * 删除组件信息
     * 
     * @param id 组件主键
     * @return 结果
     */
    public int deleteSysComponentById(Long id);

    /**
     * 取消授权
     * @param id 组件主键
     * @return
     */
    public int revokeSysComponent(Long id);
}

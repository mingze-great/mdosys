package com.mdosys.system.mapper;

import com.mdosys.system.domain.SysComponent;

import java.util.List;

/**
 * 组件Mapper接口
 * 
 * @author hwj
 * @date 2022-10-25
 */
public interface SysComponentMapper 
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
     * @param permission 权限
     * @return 组件集合
     */
    public List<SysComponent> selectPubSysComponentList(Long permission);

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
    public Long insertSysComponent(SysComponent sysComponent);

    /**
     * 修改组件
     * 
     * @param sysComponent 组件
     * @return 结果
     */
    public int updateSysComponent(SysComponent sysComponent);

    /**
     * 删除组件
     * 
     * @param id 组件主键
     * @return 结果
     */
    public int deleteSysComponentById(Long id);

    /**
     * 批量删除组件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysComponentByIds(Long[] ids);

    /**
     * 取消授权
     * @param id 组件主键
     * @return
     */
    public int revokeSysComponent(Long id);
}

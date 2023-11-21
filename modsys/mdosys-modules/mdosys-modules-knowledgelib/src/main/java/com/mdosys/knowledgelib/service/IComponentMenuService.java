package com.mdosys.knowledgelib.service;

import com.mdosys.knowledgelib.domain.ComponentMenu;

import java.util.List;

/**
 * component_menuService接口
 *
 * @author hwj
 * @date 2022-09-19
 */
public interface IComponentMenuService
{
    /**
     * 查询component_menu
     *
     * @param id component_menu主键
     * @return component_menu
     */
    public ComponentMenu selectComponentMenuById(Long id);

    /**
     * 查询component_menu列表
     *
     * @param componentMenu component_menu
     * @return component_menu集合
     */
    public List<ComponentMenu> selectComponentMenuList(ComponentMenu componentMenu);

    /**
     * 新增component_menu
     *
     * @param componentMenu component_menu
     * @return 结果
     */
    public int insertComponentMenu(ComponentMenu componentMenu);

    /**
     * 修改component_menu
     *
     * @param componentMenu component_menu
     * @return 结果
     */
    public int updateComponentMenu(ComponentMenu componentMenu);

    /**
     * 批量删除component_menu
     *
     * @param ids 需要删除的component_menu主键集合
     * @return 结果
     */
    public int deleteComponentMenuByIds(Long[] ids);

    /**
     * 删除component_menu信息
     *
     * @param id component_menu主键
     * @return 结果
     */
    public int deleteComponentMenuById(Long id);

    /**
     * 通过创建人查询component_menu
     * @param create_by 创建人id
     * @return
     */
    public List<ComponentMenu> selectMenuListByUserId(Long create_by);

    /**
     * 查询公共组件
     * @return
     */
    public List<ComponentMenu> selectPubMenuList();

    /**
     * 查询目录结构（不包括组件）
     * @return
     */
    public List<ComponentMenu> selectTreeMenuList();
}

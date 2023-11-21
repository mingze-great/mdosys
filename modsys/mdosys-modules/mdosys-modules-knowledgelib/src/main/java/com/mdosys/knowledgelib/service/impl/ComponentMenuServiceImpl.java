package com.mdosys.knowledgelib.service.impl;

import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.knowledgelib.domain.ComponentMenu;
import com.mdosys.knowledgelib.mapper.ComponentMenuMapper;
import com.mdosys.knowledgelib.service.IComponentMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * component_menuService业务层处理
 *
 * @author hwj
 * @date 2022-09-19
 */
@Service
public class ComponentMenuServiceImpl implements IComponentMenuService
{
    @Autowired
    private ComponentMenuMapper componentMenuMapper;

    /**
     * 查询component_menu
     *
     * @param id component_menu主键
     * @return component_menu
     */
    @Override
    public ComponentMenu selectComponentMenuById(Long id)
    {
        return componentMenuMapper.selectComponentMenuById(id);
    }

    /**
     * 查询component_menu列表
     *
     * @param componentMenu component_menu
     * @return component_menu
     */
    @Override
    public List<ComponentMenu> selectComponentMenuList(ComponentMenu componentMenu)
    {
        return componentMenuMapper.selectComponentMenuList(componentMenu);
    }

    /**
     * 新增component_menu
     *
     * @param componentMenu component_menu
     * @return 结果
     */
    @Override
    public int insertComponentMenu(ComponentMenu componentMenu)
    {
        componentMenu.setCreateTime(DateUtils.getNowDate());
        return componentMenuMapper.insertComponentMenu(componentMenu);
    }

    /**
     * 修改component_menu
     *
     * @param componentMenu component_menu
     * @return 结果
     */
    @Override
    public int updateComponentMenu(ComponentMenu componentMenu)
    {
        return componentMenuMapper.updateComponentMenu(componentMenu);
    }

    /**
     * 批量删除component_menu
     *
     * @param ids 需要删除的component_menu主键
     * @return 结果
     */
    @Override
    public int deleteComponentMenuByIds(Long[] ids)
    {
        return componentMenuMapper.deleteComponentMenuByIds(ids);
    }

    /**
     * 删除component_menu信息
     *
     * @param id component_menu主键
     * @return 结果
     */
    @Override
    public int deleteComponentMenuById(Long id)
    {
        return componentMenuMapper.deleteComponentMenuById(id);
    }

    @Override
    public List<ComponentMenu> selectMenuListByUserId(Long create_by) {
        return componentMenuMapper.selectMenuListByUserId(create_by);
    }

    @Override
    public List<ComponentMenu> selectPubMenuList() {
        return componentMenuMapper.selectPubMenuList();
    }

    @Override
    public List<ComponentMenu> selectTreeMenuList() {
        return componentMenuMapper.selectTreeMenuList();
    }
}

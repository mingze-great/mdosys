package com.mdosys.knowledgelib.service.impl;

import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.knowledgelib.domain.CompFile;
import com.mdosys.knowledgelib.domain.Component;
import com.mdosys.knowledgelib.mapper.CompFileMapper;
import com.mdosys.knowledgelib.mapper.ComponentMapper;
import com.mdosys.knowledgelib.service.IComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组件Service业务层处理
 *
 * @author brz
 * @date 2022-09-01
 */
@Service
public class ComponentServiceImpl implements IComponentService
{
    @Autowired
    private ComponentMapper componentMapper;

    @Autowired
    private CompFileMapper compFileMapper;

    /**
     * 查询组件
     *
     * @param compId 组件主键
     * @return 组件
     */
    @Override
    public Component selectComponentByCompId(Long compId)
    {
        return componentMapper.selectComponentByCompId(compId);
    }

    /**
     * 查询组件列表
     *
     * @param component 组件
     * @return 组件
     */
    @Override
    public List<Component> selectComponentList(Component component)
    {
        return componentMapper.selectComponentList(component);
    }

    /**
     * 公共组件列表
     *
     * @param component 组件
     * @return 组件
     */
    @Override
    public List<Component> selectPubComp(Component component) {
        return componentMapper.selectPubComp(component);
    }

    /**
     * 我的组件列表
     *
     * @param userId 用户ID
     * @return 组件
     */
    @Override
    public List<Component> selectMyCompByUserId(Long userId) {
        return componentMapper.selectMyCompByUserId(userId);
    }

    /**
     * 按类型查询组件
     *
     * @param typeId 组件类型主键
     * @return 结果
     */
    @Override
    public List<Component> selectComponentByTypeId(Long typeId) { return componentMapper.selectComponentByTypeId(typeId); }

    /**
     * 新增组件
     *
     * @param component 组件
     * @return 结果
     */
    @Override
    public int insertComponent(Component component)
    {
        component.setCreateTime(DateUtils.getNowDate());
        return componentMapper.insertComponent(component);
    }

    /**
     * 修改组件
     *
     * @param component 组件
     * @return 结果
     */
    @Override
    public int updateComponent(Component component)
    {
        return componentMapper.updateComponent(component);
    }

    /**
     * 批量删除组件
     *
     * @param compIds 需要删除的组件主键
     * @return 结果
     */
    @Override
    public int deleteComponentByCompIds(Long[] compIds)
    {
        return componentMapper.deleteComponentByCompIds(compIds);
    }

    /**
     * 删除组件信息
     *
     * @param compId 组件主键
     * @return 结果
     */
    @Override
    public int deleteComponentByCompId(Long compId)
    {
        return componentMapper.deleteComponentByCompId(compId);
    }

    /**
     * 新增组件文件信息
     * @param compFile
     * @return
     */
    @Override
    public int insertCompFile(CompFile compFile) {
        return compFileMapper.insertCompFile(compFile);
    }

    /**
     * 查询组件列表
     *
     * @param compName
     * @return
     */
    @Override
    public List<CompFile> selectCompFileByCompId(Long compId) {
        return compFileMapper.selectCompFileByCompId(compId);
    }

    /**
     * 获取当前自增id
     *
     * @return
     */
    @Override
    public Long selectLastInsertId() {
        return componentMapper.selectLastInsertId();
    }

    /**
     * 查询文件id
     *
     * @param compId
     * @return
     */
    @Override
    public List<Long> selectFileIdByCompId(Long compId) {
        return compFileMapper.selectFileIdByCompId(compId);
    }

    /**
     * 修改组件文件信息
     *
     * @param compFile
     * @return
     */
    @Override
    public void updateCompFile(CompFile compFile) {
        deleteCompFileByCompId(compFile.getCompId());
        insertCompFile(compFile);
    }

    /**
     * 删除组件文件信息
     *
     * @param compId
     * @return
     */
    @Override
    public int deleteCompFileByCompId(Long compId){
        return compFileMapper.deleteCompFileByCompId(compId);
    }
}

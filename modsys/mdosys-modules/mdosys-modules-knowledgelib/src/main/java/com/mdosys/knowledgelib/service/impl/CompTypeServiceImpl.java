package com.mdosys.knowledgelib.service.impl;

import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.knowledgelib.domain.CompType;
import com.mdosys.knowledgelib.mapper.CompTypeMapper;
import com.mdosys.knowledgelib.service.ICompTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组件类型Service业务层处理
 *
 * @author brz
 * @date 2022-09-02
 */
@Service
public class CompTypeServiceImpl implements ICompTypeService 
{
    @Autowired
    private CompTypeMapper compTypeMapper;

    /**
     * 查询组件类型
     *
     * @param typeId 组件类型主键
     * @return 组件类型
     */
    @Override
    public CompType selectCompTypeByTypeId(Long typeId)
    {
        return compTypeMapper.selectCompTypeByTypeId(typeId);
    }

    /**
     * 查询组件类型列表
     *
     * @param compType 组件类型
     * @return 组件类型
     */
    @Override
    public List<CompType> selectCompTypeList(CompType compType)
    {
        return compTypeMapper.selectCompTypeList(compType);
    }

    /**
     * 新增组件类型
     *
     * @param compType 组件类型
     * @return 结果
     */
    @Override
    public int insertCompType(CompType compType)
    {
        compType.setCreateTime(DateUtils.getNowDate());
        return compTypeMapper.insertCompType(compType);
    }

    /**
     * 修改组件类型
     *
     * @param compType 组件类型
     * @return 结果
     */
    @Override
    public int updateCompType(CompType compType)
    {
        return compTypeMapper.updateCompType(compType);
    }

    /**
     * 批量删除组件类型
     *
     * @param typeIds 需要删除的组件类型主键
     * @return 结果
     */
    @Override
    public int deleteCompTypeByTypeIds(Long[] typeIds)
    {
        return compTypeMapper.deleteCompTypeByTypeIds(typeIds);
    }

    /**
     * 删除组件类型信息
     *
     * @param typeId 组件类型主键
     * @return 结果
     */
    @Override
    public int deleteCompTypeByTypeId(Long typeId)
    {
        return compTypeMapper.deleteCompTypeByTypeId(typeId);
    }
}

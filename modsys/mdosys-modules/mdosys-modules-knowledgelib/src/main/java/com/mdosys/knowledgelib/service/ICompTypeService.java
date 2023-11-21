package com.mdosys.knowledgelib.service;

import com.mdosys.knowledgelib.domain.CompType;

import java.util.List;

/**
 * 组件类型Service接口
 *
 * @author brz
 * @date 2022-09-02
 */
public interface ICompTypeService
{
    /**
     * 查询组件类型
     *
     * @param typeId 组件类型主键
     * @return 组件类型
     */
    public CompType selectCompTypeByTypeId(Long typeId);

    /**
     * 查询组件类型列表
     *
     * @param compType 组件类型
     * @return 组件类型集合
     */
    public List<CompType> selectCompTypeList(CompType compType);

    /**
     * 新增组件类型
     *
     * @param compType 组件类型
     * @return 结果
     */
    public int insertCompType(CompType compType);

    /**
     * 修改组件类型
     *
     * @param compType 组件类型
     * @return 结果
     */
    public int updateCompType(CompType compType);

    /**
     * 批量删除组件类型
     *
     * @param typeIds 需要删除的组件类型主键集合
     * @return 结果
     */
    public int deleteCompTypeByTypeIds(Long[] typeIds);

    /**
     * 删除组件类型信息
     *
     * @param typeId 组件类型主键
     * @return 结果
     */
    public int deleteCompTypeByTypeId(Long typeId);
}

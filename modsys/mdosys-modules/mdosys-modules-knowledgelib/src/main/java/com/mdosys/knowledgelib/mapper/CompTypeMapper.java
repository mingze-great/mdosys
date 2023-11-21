package com.mdosys.knowledgelib.mapper;

import com.mdosys.knowledgelib.domain.CompType;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 组件类型Mapper接口
 *
 * @author brz
 * @date 2022-09-02
 */
@Component
public interface CompTypeMapper
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
     * 删除组件类型
     *
     * @param typeId 组件类型主键
     * @return 结果
     */
    public int deleteCompTypeByTypeId(Long typeId);

    /**
     * 批量删除组件类型
     *
     * @param typeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCompTypeByTypeIds(Long[] typeIds);
}

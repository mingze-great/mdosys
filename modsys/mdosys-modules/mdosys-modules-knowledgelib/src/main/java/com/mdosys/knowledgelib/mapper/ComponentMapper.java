package com.mdosys.knowledgelib.mapper;

import com.mdosys.knowledgelib.domain.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 组件Mapper接口
 *
 * @author brz
 * @date 2022-09-01
 */
@org.springframework.stereotype.Component
public interface ComponentMapper
{
    /**
     * 查询组件
     *
     * @param compId 组件主键
     * @return 组件
     */
    public Component selectComponentByCompId(Long compId);

    /**
     * 查询组件列表
     *
     * @param component 组件
     * @return 组件集合
     */
    public List<Component> selectComponentList(Component component);

    /**
     * 公共组件列表
     *
     * @param component 组件类型主键
     * @return 组件集合
     */
    public List<Component> selectPubComp(Component component);

    /**
     * 私有组件列表
     *
     * @param userId 组件类型主键
     * @return 组件集合
     */
    public List<Component> selectMyCompByUserId(Long userId);

    /**
     * 按类型查询组件列表
     *
     * @param typeId 组件类型主键
     * @return 组件集合
     */
    public List<Component> selectComponentByTypeId(Long typeId);

    /**
     * 新增组件
     *
     * @param component 组件
     * @return 结果
     */
    public int insertComponent(Component component);

    /**
     * 修改组件
     *
     * @param component 组件
     * @return 结果
     */
    public int updateComponent(Component component);

    /**
     * 删除组件
     *
     * @param compId 组件主键
     * @return 结果
     */
    public int deleteComponentByCompId(Long compId);

    /**
     * 批量删除组件
     *
     * @param compIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteComponentByCompIds(Long[] compIds);

    /**
     * 获取当前自增id
     * @return
     */
    public Long selectLastInsertId();

}

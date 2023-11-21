package com.mdosys.knowledgelib.service;

import com.mdosys.knowledgelib.domain.CompFile;
import com.mdosys.knowledgelib.domain.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 组件Service接口
 *
 * @author brz
 * @date 2022-09-01
 */
public interface IComponentService
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
     * 按类型查询公共组件列表
     *
     * @param component 组件
     * @return 组件集合
     */
    public List<Component> selectPubComp(Component component);

    /**
     * 按类型查询私有组件列表
     *
     * @param userId 组件
     * @return 组件集合
     */
    public List<Component> selectMyCompByUserId(Long userId);

    /**
     * 按类型查询组件列表
     *
     * @param typeId 组件
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
     * 批量删除组件
     *
     * @param compIds 需要删除的组件主键集合
     * @return 结果
     */
    public int deleteComponentByCompIds(Long[] compIds);

    /**
     * 删除组件信息
     *
     * @param compId 组件主键
     * @return 结果
     */
    public int deleteComponentByCompId(Long compId);

    /**
     * 新增组件文件信息
     *
     * @param compFile
     * @return
     */
    public int insertCompFile(CompFile compFile);

    /**
     * 查询组件文件信息
     *
     * @param compId
     * @return
     */
    public List<CompFile> selectCompFileByCompId(Long compId);

    /**
     * 获取当前自增id
     *
     * @return
     */
    public Long selectLastInsertId();

    /**
     * 查询文件id
     * @param compId
     * @return
     */
    public List<Long> selectFileIdByCompId(Long compId);

    /**
     * 修改组件文件信息
     * @param compFile
     * @return
     */
    public void updateCompFile(CompFile compFile);

    /**
     * 删除组件文件信息
     *
     * @param compId
     * @return
     */
    public int deleteCompFileByCompId(Long compId);
}

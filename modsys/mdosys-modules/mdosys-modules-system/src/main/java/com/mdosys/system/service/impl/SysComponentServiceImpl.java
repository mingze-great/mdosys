package com.mdosys.system.service.impl;

import java.util.List;

import com.mdosys.common.core.exception.ServiceException;
import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.common.core.utils.uuid.CodeUtils;
import com.mdosys.common.core.web.domain.AjaxResult;
import com.mdosys.system.api.RemoteFileService;
import com.mdosys.system.domain.Enum.ComponentTypeEnum;
import com.mdosys.system.domain.SysComponent;
import com.mdosys.system.mapper.SysComponentMapper;
import com.mdosys.system.service.ISysComponentFileService;
import com.mdosys.system.service.ISysComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组件Service业务层处理
 * 
 * @author hwj
 * @date 2022-10-25
 */
@Service
public class SysComponentServiceImpl implements ISysComponentService
{
    @Autowired
    private SysComponentMapper sysComponentMapper;

    @Autowired
    private RemoteFileService fileService;

    @Value("${minio.compBucket}")
    private String bucket;

    @Autowired
    private ISysComponentFileService sysComponentFileService;

    /**
     * 查询组件
     * 
     * @param id 组件主键
     * @return 组件
     */
    @Override
    public SysComponent selectSysComponentById(Long id)
    {
        return sysComponentMapper.selectSysComponentById(id);
    }

    /**
     * 查询组件列表
     * 
     * @param sysComponent 组件
     * @return 组件
     */
    @Override
    public List<SysComponent> selectSysComponentList(SysComponent sysComponent)
    {
        return sysComponentMapper.selectSysComponentList(sysComponent);
    }

    /**
     * 查询公共组件列表
     * @return 组件集合
     */
    @Override
    public List<SysComponent> selectPubSysComponentList(){
        Long permission = (long)2;
        return sysComponentMapper.selectPubSysComponentList(permission);
    }

    /**
     * 查询组件菜单列表
     *
     * @return 组件
     */
    @Override
    public List<SysComponent> selectSysComponentMenuList() {
        return sysComponentMapper.selectSysComponentMenuList();
    }

    /**
     * 新增组件
     * 
     * @param sysComponent 组件
     * @return 结果
     */
    @Override
    public Long insertSysComponent(SysComponent sysComponent){
        try {
            sysComponent.setCode(CodeUtils.getInstance().genCode());
            sysComponent.setType(ComponentTypeEnum.COMPONENT);
            sysComponent.setCreateTime(DateUtils.getNowDate());
            sysComponent.setUpdateTime(DateUtils.getNowDate());
            sysComponentMapper.insertSysComponent(sysComponent);
            return sysComponent.getId();
        } catch (CodeUtils.CodeGenerateException error){
            return 0L;
        }
    }

    /**
     * 修改组件
     * 
     * @param sysComponent 组件
     * @return 结果
     */
    @Override
    public int updateSysComponent(SysComponent sysComponent) {
        sysComponent.setUpdateTime(DateUtils.getNowDate());
        return sysComponentMapper.updateSysComponent(sysComponent);
    }

    /**
     * 批量删除组件
     * 
     * @param ids 需要删除的组件主键
     * @return 结果
     */
    @Override
    public int deleteSysComponentByIds(Long[] ids)
    {
        return sysComponentMapper.deleteSysComponentByIds(ids);
    }

    /**
     * 删除组件信息
     * 
     * @param id 组件主键
     * @return 结果
     */
    @Override
    public int deleteSysComponentById(Long id) {
        List<String> paths = sysComponentFileService.selectFilePathsByCompId(id);
        for (String path : paths) {
            fileService.deleteFile(path);
        }
        sysComponentFileService.deleteSysComponentFileByCompId(id);
        return sysComponentMapper.deleteSysComponentById(id);
    }

    /**
     * 取消授权
     * @param id 组件主键
     * @return
     */
    @Override
    public int revokeSysComponent(Long id){
        return sysComponentMapper.revokeSysComponent(id);
    }
}

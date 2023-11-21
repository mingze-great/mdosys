package com.mdosys.system.service.impl;

import com.mdosys.common.core.utils.DateUtils;
import com.mdosys.system.domain.SysComponentMenu;
import com.mdosys.system.mapper.SysComponentMenuMapper;
import com.mdosys.system.service.ISysComponentMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysComponentMenuServiceImpl implements ISysComponentMenuService {

    @Autowired
    SysComponentMenuMapper sysComponentMenuMapper;

    @Override
    public List<SysComponentMenu> selectSysComponentMenuList(SysComponentMenu sysComponentMenu) {
        return sysComponentMenuMapper.selectSysComponentMenuList(sysComponentMenu);
    }

    @Override
    public Integer insertSysComponentMenu(SysComponentMenu sysComponentMenu) {
        sysComponentMenu.setCreateTime(DateUtils.getNowDate());
        sysComponentMenu.setUpdateTime(DateUtils.getNowDate());
        System.out.println(sysComponentMenu);
        return sysComponentMenuMapper.insertSysComponentMenu(sysComponentMenu);
    }

    @Override
    public Integer deleteSysComponentMenuById(Long id) {
        return sysComponentMenuMapper.deleteSysComponentMenuById(id);
    }

    @Override
    public Integer updateSysComponentMenu(SysComponentMenu sysComponentMenu) {
        return sysComponentMenuMapper.updateSysComponentMenu(sysComponentMenu);
    }
}
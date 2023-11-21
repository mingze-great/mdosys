package com.mdosys.system.mapper;

import com.mdosys.system.domain.SysComponentMenu;

import java.util.List;

public interface SysComponentMenuMapper {

    List<SysComponentMenu> selectSysComponentMenuList(SysComponentMenu sysComponentMenu);

    Integer insertSysComponentMenu(SysComponentMenu sysComponentMenu);

    Integer deleteSysComponentMenuById(Long id);

    Integer updateSysComponentMenu(SysComponentMenu sysComponentMenu);
}

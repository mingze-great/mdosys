package com.mdosys.system.service;

import com.mdosys.system.domain.SysComponentMenu;

import java.util.List;

public interface ISysComponentMenuService {

    List<SysComponentMenu> selectSysComponentMenuList(SysComponentMenu sysComponentMenu);

    Integer insertSysComponentMenu(SysComponentMenu sysComponentMenu);

    Integer deleteSysComponentMenuById(Long id);

    Integer updateSysComponentMenu(SysComponentMenu sysComponentMenu);

}

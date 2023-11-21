package com.mdosys.system.service;

import com.mdosys.common.core.enums.Paraminfo;
import com.mdosys.system.domain.SysComponent;
import com.mdosys.system.domain.SysComponentFile;

import java.awt.*;
import java.util.List;

public interface ISysParamInfoService {
    Integer insertSysParamInfo(Paraminfo paraminfo);

    List<Paraminfo> selectSysParamList(SysComponentFile sysComponentFile);
}

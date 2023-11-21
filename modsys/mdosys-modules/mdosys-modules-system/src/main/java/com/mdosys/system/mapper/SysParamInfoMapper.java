package com.mdosys.system.mapper;

import com.mdosys.common.core.enums.Paraminfo;
import com.mdosys.system.domain.SysComponent;
import com.mdosys.system.domain.SysComponentFile;

import java.awt.*;
import java.util.List;

public interface SysParamInfoMapper {

    List<Paraminfo> selectSysParamList(SysComponentFile sysComponentFile);
    Integer insertSysParamInfo(Paraminfo paraminfo);
}

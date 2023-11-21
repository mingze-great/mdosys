package com.mdosys.system.service.impl;

import com.mdosys.common.core.enums.Paraminfo;
import com.mdosys.system.domain.SysComponent;
import com.mdosys.system.domain.SysComponentFile;
import com.mdosys.system.mapper.SysParamInfoMapper;
import com.mdosys.system.service.ISysParamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class SysParamInfoServiceImpl implements ISysParamInfoService {

    @Autowired
    SysParamInfoMapper paramInfoMapper;

    public Integer insertSysParamInfo(Paraminfo paraminfo){
        return paramInfoMapper.insertSysParamInfo(paraminfo);
    }

    @Override
    public List<Paraminfo> selectSysParamList(SysComponentFile sysComponentFile) {
        return paramInfoMapper.selectSysParamList(sysComponentFile);
    }
}

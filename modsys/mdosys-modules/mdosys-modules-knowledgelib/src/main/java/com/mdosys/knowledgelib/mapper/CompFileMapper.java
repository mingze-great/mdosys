package com.mdosys.knowledgelib.mapper;

import com.mdosys.knowledgelib.domain.CompFile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CompFileMapper {
    public int insertCompFile(CompFile compFile);

    public List<CompFile> selectCompFileByCompId(Long compId);

    public List<Long> selectFileIdByCompId(Long compId);

//    public int updateCompFile(CompFile compFile);

    /**
     * 删除组件文件
     *
     * @return
     */
    public int deleteCompFileByCompId(Long compId);
}

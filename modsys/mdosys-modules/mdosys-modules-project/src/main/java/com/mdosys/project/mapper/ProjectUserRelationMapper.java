package com.mdosys.project.mapper;


import com.mdosys.project.domain.ProjectUserRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProjectUserRelationMapper {

    int insertProjectUserRelation(ProjectUserRelation projectUserRelation);

    ProjectUserRelation selectProjectRelation(@Param("projectId") long projectId, @Param("userId") long userId);

}

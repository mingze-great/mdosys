package com.mdosys.project.controller;

import com.mdosys.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("projects/{projectCode}/task-definition")
public class TaskDefinitionController {


    @PostMapping
    public AjaxResult createTaskDefinition(@PathVariable long projectCode){

        return  AjaxResult.success();
    }

}

package com.mdosys.project.controller;

import com.mdosys.project.service.IProcessDefinitionService;
import com.mdosys.project.service.impl.ProcessDefinitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects/{projectCode}/process-definition")
public class ProcessDefinitionController {
    private IProcessDefinitionService processDefinitionService;

    @Autowired
    public void setProcessDefinitionService(IProcessDefinitionService processDefinitionService) {
        this.processDefinitionService = processDefinitionService;
    }
}

package com.mdosys.scheduler.common.model;

import com.mdosys.scheduler.common.enums.DependResult;
import com.mdosys.scheduler.common.enums.ExecutionStatus;

/**
 * dependent item
 */
public class DependentItem {
    private long projectCode;
    private long definitionCode;
    private long depTaskCode;
    private String cycle;
    private String dateValue;
    private DependResult dependResult;
    private ExecutionStatus status;

    public String getKey() {
        return String.format("%d-%d-%s-%s",
                getDefinitionCode(),
                getDepTaskCode(),
                getCycle(),
                getDateValue());
    }

    public long getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(long projectCode) {
        this.projectCode = projectCode;
    }

    public long getDefinitionCode() {
        return definitionCode;
    }

    public void setDefinitionCode(long definitionCode) {
        this.definitionCode = definitionCode;
    }

    public long getDepTaskCode() {
        return depTaskCode;
    }

    public void setDepTaskCode(long depTaskCode) {
        this.depTaskCode = depTaskCode;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    public DependResult getDependResult() {
        return dependResult;
    }

    public void setDependResult(DependResult dependResult) {
        this.dependResult = dependResult;
    }

    public ExecutionStatus getStatus() {
        return status;
    }

    public void setStatus(ExecutionStatus status) {
        this.status = status;
    }
}

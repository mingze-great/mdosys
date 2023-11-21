package com.mdosys.scheduler.common.model;

import com.mdosys.scheduler.common.enums.DependentRelation;

import java.util.List;

public class DependentTaskModel {


    private List<DependentItem> dependItemList;
    private DependentRelation relation;

    public List<DependentItem> getDependItemList() {
        return dependItemList;
    }

    public void setDependItemList(List<DependentItem> dependItemList) {
        this.dependItemList = dependItemList;
    }

    public DependentRelation getRelation() {
        return relation;
    }

    public void setRelation(DependentRelation relation) {
        this.relation = relation;
    }
}

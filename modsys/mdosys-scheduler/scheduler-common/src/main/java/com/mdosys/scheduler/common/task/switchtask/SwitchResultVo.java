package com.mdosys.scheduler.common.task.switchtask;

import java.util.ArrayList;
import java.util.List;

public class SwitchResultVo {

    private String condition;
    private List<String> nextNode;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<String> getNextNode() {
        return nextNode;
    }

    public void setNextNode(Object nextNode) {
        if (nextNode instanceof String) {
            List<String> nextNodeList = new ArrayList<>();
            nextNodeList.add(String.valueOf(nextNode));
            this.nextNode = nextNodeList;
        } else if (nextNode instanceof Number) {
            List<String> nextNodeList = new ArrayList<>();
            nextNodeList.add(nextNode.toString());
            this.nextNode = nextNodeList;
        } else {
            this.nextNode = (ArrayList) nextNode;
        }
    }
}
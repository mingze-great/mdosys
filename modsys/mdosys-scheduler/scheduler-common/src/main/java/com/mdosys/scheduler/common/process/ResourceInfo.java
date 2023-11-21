package com.mdosys.scheduler.common.process;

/**
 * resource info
 */
public class ResourceInfo {
    /**
     * res id of the resource that was uploaded
     */
    private int id;

    private String res;

    /**
     * full name of the resource that was uploaded
     */
    private String resourceName;

    public ResourceInfo() {
        // do nothing, void constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}

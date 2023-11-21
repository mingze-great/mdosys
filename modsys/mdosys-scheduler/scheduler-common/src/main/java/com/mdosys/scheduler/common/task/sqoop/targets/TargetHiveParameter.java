package com.mdosys.scheduler.common.task.sqoop.targets;

/**
 * target hive parameter
 */
public class TargetHiveParameter {

    /**
     * hive database
     */
    private String hiveDatabase;
    /**
     * hive table
     */
    private String hiveTable;
    /**
     * create hive table
     */
    private boolean createHiveTable;
    /**
     * drop delimiter
     */
    private boolean dropDelimiter;
    /**
     * hive overwrite
     */
    private boolean hiveOverWrite;
    /**
     * replace delimiter
     */
    private String replaceDelimiter;
    /**
     * hive partition key
     */
    private String hivePartitionKey;
    /**
     * hive partition value
     */
    private String hivePartitionValue;

    public String getHiveDatabase() {
        return hiveDatabase;
    }

    public void setHiveDatabase(String hiveDatabase) {
        this.hiveDatabase = hiveDatabase;
    }

    public String getHiveTable() {
        return hiveTable;
    }

    public void setHiveTable(String hiveTable) {
        this.hiveTable = hiveTable;
    }

    public boolean isCreateHiveTable() {
        return createHiveTable;
    }

    public void setCreateHiveTable(boolean createHiveTable) {
        this.createHiveTable = createHiveTable;
    }

    public boolean isDropDelimiter() {
        return dropDelimiter;
    }

    public void setDropDelimiter(boolean dropDelimiter) {
        this.dropDelimiter = dropDelimiter;
    }

    public boolean isHiveOverWrite() {
        return hiveOverWrite;
    }

    public void setHiveOverWrite(boolean hiveOverWrite) {
        this.hiveOverWrite = hiveOverWrite;
    }

    public String getReplaceDelimiter() {
        return replaceDelimiter;
    }

    public void setReplaceDelimiter(String replaceDelimiter) {
        this.replaceDelimiter = replaceDelimiter;
    }

    public String getHivePartitionKey() {
        return hivePartitionKey;
    }

    public void setHivePartitionKey(String hivePartitionKey) {
        this.hivePartitionKey = hivePartitionKey;
    }

    public String getHivePartitionValue() {
        return hivePartitionValue;
    }

    public void setHivePartitionValue(String hivePartitionValue) {
        this.hivePartitionValue = hivePartitionValue;
    }
}

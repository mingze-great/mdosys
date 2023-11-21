package com.mdosys.scheduler.common.task.sqoop.sources;

/**
 * source hive parameter
 */
public class SourceHiveParameter {

    /**
     * hive database
     */
    private String hiveDatabase;
    /**
     * hive table
     */
    private String hiveTable;
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

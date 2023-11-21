package com.mdosys.common.core.enums;

import java.io.Serializable;

public class Paraminfo extends BasicParam implements Serializable {
    private String id=null;

    private Long fileId=null;
    private String name=null;
    private String value=null;
    private String iotype=null;
    private String sign=null;
    private String type=null;
    private String unit = "";
    private Double lowerLimit = 0.0;
    private Double upperLimit = 0.0;
    private String columnNames = "";
    private String rowNames = "";
    private String enumCons = "";
    private Integer rows = 0;
    private Integer columns = 0;
    private String remark = "";
    private Boolean result = true;

    public Paraminfo() {
    }

    public Paraminfo(String id) {
        this.id = id;
    }

    public Paraminfo(String name, String value, String sign, String type, String iotype, String initiotype, String unit, String url) {
        this.name = name;
        this.value = value;
        this.sign = sign;
        this.type = type;
        this.iotype = iotype;
        this.initiotype = initiotype;
        this.unit = unit;
        this.url = url;
    }

    public Paraminfo(String name, String value, String sign, String type) {
        this.name = name;
        this.value = value;
        this.sign = sign;
        this.type = type;
    }

    public Paraminfo(String name, String value, String sign, String type, String unit) {
        this.name = name;
        this.value = value;
        this.sign = sign;
        this.type = type;
        this.unit = unit;
    }

    public Paraminfo(String name, String value, String sign, String type, String unit, String url, String columnNames) {
        this.name = name;
        this.value = value;
        this.sign = sign;
        this.type = type;
        this.unit = unit;
        this.url = url;
        this.columnNames = columnNames;
    }

    public Paraminfo(String name, String value, String sign, String type, String unit, String url) {
        this.name = name;
        this.value = value;
        this.sign = sign;
        this.type = type;
        this.unit = unit;
        this.url = url;
    }

    public Paraminfo(String id, String name, String value, String iotype, String sign, String type, String unit, String columnNames, String rowNames, Integer rows, Integer columns, String enumCons, String remark) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.iotype = iotype;
        this.initiotype = iotype;
        this.sign = sign;
        this.type = type;
        this.unit = unit;
        this.columnNames = columnNames;
        this.rowNames = rowNames;
        this.rows = rows;
        this.columns = columns;
        this.enumCons = enumCons;
        this.remark = remark;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
        this.result = true;
    }

    public String getIotype() {
        return this.iotype;
    }

    public void setIotype(String iotype) {
        this.iotype = iotype;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getLowerLimit() {
        return this.lowerLimit;
    }

    public void setLowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public Double getUpperLimit() {
        return this.upperLimit;
    }

    public void setUpperLimit(Double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getColumnNames() {
        return this.columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public String getRowNames() {
        return this.rowNames;
    }

    public void setRowNames(String rowNames) {
        this.rowNames = rowNames;
    }

    public String getEnumCons() {
        return this.enumCons;
    }

    public void setEnumCons(String enumCons) {
        this.enumCons = enumCons;
    }

    public Integer getRows() {
        return this.rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return this.columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFullName() {
        return (this.docName == null ? "" : this.docName + ".") + this.name;
    }

    public boolean isResult() {
        return this.result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getFileId() {
        return this.fileId;
    }
    @Override
    public String toString() {
        return "Paraminfo{" +
                "id='" + id + '\'' +
                ", fileId='" + fileId + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", iotype='" + iotype + '\'' +
                ", sign='" + sign + '\'' +
                ", type='" + type + '\'' +
                ", unit='" + unit + '\'' +
                ", lowerLimit=" + lowerLimit +
                ", upperLimit=" + upperLimit +
                ", columnNames='" + columnNames + '\'' +
                ", rowNames='" + rowNames + '\'' +
                ", enumCons='" + enumCons + '\'' +
                ", rows=" + rows +
                ", columns=" + columns +
                ", remark='" + remark + '\'' +
                ", result=" + result +
                '}';
    }
}

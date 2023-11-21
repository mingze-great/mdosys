package com.mdosys.scheduler.common.task.sqoop.targets;

/**
 * target hdfs parameter
 */
public class TargetHdfsParameter {

    /**
     * target dir
     */
    private String targetPath;
    /**
     * delete target dir
     */
    private boolean deleteTargetDir;
    /**
     * file type
     */
    private String fileType;
    /**
     * compression codec
     */
    private String compressionCodec;
    /**
     * fields terminated
     */
    private String fieldsTerminated;
    /**
     * lines terminated
     */
    private String linesTerminated;

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean isDeleteTargetDir() {
        return deleteTargetDir;
    }

    public void setDeleteTargetDir(boolean deleteTargetDir) {
        this.deleteTargetDir = deleteTargetDir;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCompressionCodec() {
        return compressionCodec;
    }

    public void setCompressionCodec(String compressionCodec) {
        this.compressionCodec = compressionCodec;
    }

    public String getFieldsTerminated() {
        return fieldsTerminated;
    }

    public void setFieldsTerminated(String fieldsTerminated) {
        this.fieldsTerminated = fieldsTerminated;
    }

    public String getLinesTerminated() {
        return linesTerminated;
    }

    public void setLinesTerminated(String linesTerminated) {
        this.linesTerminated = linesTerminated;
    }

}

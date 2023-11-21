package com.mdosys.common.core.utils.file;

import java.text.DecimalFormat;

public class FileSizeUtils {

    /**
     * 文件单位转换：字节转K，M，G
     * @param size
     * @return
     */
    public static String getFileSize(Long size){
        DecimalFormat df = new DecimalFormat("#.0");
        if(size>=1024*1024*1024){
            return df.format((double)size / 1073741824L)+"GB";
        } else if (size>=1024*1024){
            return df.format((double)size /1048576L)+"MB";
        }else if(size>=1024){
            return df.format((double)size /1024)+"KB";
        }else return size+"B";
    }
}

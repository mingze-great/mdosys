package com.mdosys.file.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    /**
     * 获取指定文件夹下所有文件，不含文件夹里的文件
     *
     * @param dirFilePath 文件夹路径
     * @return
     */
    public static final List<MultipartFile> getFileByDirPath(String dirFilePath) throws IOException {
        if (dirFilePath == null || dirFilePath.equals(""))
            return null;
        return getAllFile(new File(dirFilePath));
    }

    /**
     * 获取指定文件夹下所有文件，不含文件夹里的文件
     *
     * @param dirFile 文件夹
     * @return
     */
    public static List<MultipartFile> getAllFile(File dirFile) throws IOException {
        // 如果文件夹不存在或着不是文件夹，则返回 null
        if (Objects.requireNonNull(dirFile.list()).length == 0 || !dirFile.exists() || dirFile.isFile())
            return null;

        File[] childrenFiles = dirFile.listFiles();
        if (Objects.requireNonNull(dirFile.list()).length == 0 || childrenFiles.length == 0)
            return null;

        List<MultipartFile> files = new ArrayList<>();
        for (File childFile : childrenFiles) {
            // 如果是文件，直接添加到结果集合
            if (childFile.isFile()) {
                FileInputStream fileInputStream = new FileInputStream(childFile);
                MultipartFile multipartFile = new MockMultipartFile(childFile.getName(), childFile.getName(),
                        "application/octet-stream", fileInputStream);
                files.add(multipartFile);
            }
            //以下几行代码取消注释后可以将所有子文件夹里的文件也获取到列表里
//            else {
//                // 如果是文件夹，则将其内部文件添加进结果集合
//                List<File> cFiles = getAllFile(childFile);
//                if (Objects.isNull(cFiles) || cFiles.isEmpty()) continue;
//                files.addAll(cFiles);
//            }
        }
        return files;
    }


}

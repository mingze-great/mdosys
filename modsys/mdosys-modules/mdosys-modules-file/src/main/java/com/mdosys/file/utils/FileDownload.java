package com.mdosys.file.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * 下载类
 *
 */
public class FileDownload {

//    public static void downLoadMaterialFile(HttpServletResponse response, List<InputStream> inputStreamList, String dataName, List<String> fileName) throws Exception {
//        File file = compressedFileToZip(dataName, inputStreamList, fileName);
//        writeFileToRes(response, file.getName(), file);
//    }
//
//
//    private static File inputStreamToFile(InputStream stream) throws Exception {
//        //压缩包具体名称（拼接时间戳防止重名）
//        String zipFileName = datamName + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip";
//        //生成压缩包存储地址（最后会删掉）
////        String fileZip = "D:/" + zipFileName;
//        OutputStream os = null;
//        ZipOutputStream zos = null ;
//        File file = new File(zipFileName);
//        try {
////            if (!file.getParentFile().exists()) {
////                file.getParentFile().mkdirs();
////            }
//            os=new FileOutputStream(file);
//            //压缩文件
//            zos = new ZipOutputStream(os);
//            byte[] buf = new byte[1024];
//            int i = 0;
//            for (InputStream inputStream : inputStreamList) {
////                File tempFile = new File(filePath);
//                //在压缩包中添加文件夹
//                //zos.putNextEntry(new ZipEntry("测试/"+tempFile.getName()));
//                //直接在压缩包中添加文件
//                zos.putNextEntry(new ZipEntry(fileName.get(i)));
//                int len;
//                while ((len = inputStream.read(buf)) != -1){
//                    zos.write(buf, 0, len);
//                }
//                zos.closeEntry();
//                inputStream.close();
//                i++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
////            logger.info(e.toString());
//            throw new Exception("文件打包:"+e.getMessage());
//        }finally {
//            //关闭流
//            if(zos != null){
//                try {
//                    zos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            //关闭流
//            if(os!= null){
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return file;
//    }



}

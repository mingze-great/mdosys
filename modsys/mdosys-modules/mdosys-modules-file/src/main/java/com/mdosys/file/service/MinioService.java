package com.mdosys.file.service;

import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class MinioService {

    @Autowired
    MinioClient client;

    /**
     * 上传文件到minio中
     * @param file 文件
     * @param bucket 桶名
     * @return 文件地址
     * @throws Exception
     */
    public String uploadFiles(MultipartFile file, String bucket) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())){
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
        String path = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String suffix = format.format(date);
        String uuid = UUID.randomUUID().toString();
        String fileName = suffix + "/" + uuid + file.getOriginalFilename();
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .build();
        client.putObject(args);
        path = bucket + ":" + fileName;
        return path;
    }

    /**
     * 删除组件文件
     * @param path 文件路径
     */
    public void deleteFile(String path) throws Exception {
        String bucket = path.substring(0,path.indexOf(':'));
        path = path.substring(path.indexOf(':')+1);
        client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(path).build());
    }


    public void downloadComponent(String path, HttpServletResponse response){
        String bucket = path.substring(0,path.indexOf(':'));
        path = path.substring(path.indexOf(':')+1);
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build();
        try {
            InputStream in = client.getObject(args);
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InputStream downloadFile(String path){
        InputStream in = null;
        String bucket = path.substring(0,path.indexOf(':'));
        path = path.substring(path.indexOf(':')+1);
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build();
        try {
             in = client.getObject(args);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }
    
}

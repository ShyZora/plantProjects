package com.education.media;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.FilterInputStream;

public class MinioTest {

    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://127.0.0.1:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

   //上传文件
    @Test
    public  void upload() {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("testbucket")
                    //.object("test001.mp4")
                    .object("001/test001.mp4")//添加子目录
                    .filename("C:\\Users\\world\\Videos\\Captures\\Project1 - Microsoft Visual Studio 2023-02-24 13-04-50.mp4")
                    .contentType("video/mp4")//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

    }
    //查询文件


}

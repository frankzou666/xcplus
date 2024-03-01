package com.example.media;

import com.alibaba.fastjson.util.IOUtils;
import io.minio.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MinioTest {

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.1.146:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void testUpload() throws  Exception{
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://192.168.1.146:9000")
                        .credentials("minioadmin", "minioadmin")
                        .build();

        minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket("testbucket")
                        .object("01/vue.pdf")
                        .filename("/Users/oliver/Downloads/vue.pdf")
                        .build());

    }

    @Test
    public void testDown() throws  Exception{
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://192.168.1.146:9000")
                        .credentials("minioadmin", "minioadmin")
                        .build();
        //得到输入流
        InputStream stream =
                minioClient.getObject(
                        GetObjectArgs.builder().bucket("testbucket").object("vue.pdf").build());

        //创建输出流
        File copied = new File("/Users/oliver/Downloads/2.pdf");
        FileOutputStream fs = new FileOutputStream(copied);
        OutputStream fileOs = new BufferedOutputStream(fs);
        //读写文件
        byte[] buf = new byte[16384];
        int bytesRead;
        while ((bytesRead = stream.read(buf, 0, buf.length)) >= 0) {
            fileOs.write(buf,0,bytesRead);
            fileOs.flush();
        }
        fs.close();
    }


    //将文件上传到minio

    @Test
    public  void uploadTest() throws Exception{


        File file = new File("/Users/oliver/Downloads/day01.mp4");
        String chunkFilePath = "/Users/oliver/Downloads/";
        //使用文件流
        for (int i = 0; i < 10; i++) {
            //上传到minio
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("testbucket")
                            .object("01/temp_"+i)
                            .filename(chunkFilePath+"/temp_"+i)
                            .build());

        }


    }
    //文件合并
    @Test
    public void mergeMinio() throws  Exception{
        String bucket = "testbucket";
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://192.168.1.146:9000")
                        .credentials("minioadmin", "minioadmin")
                        .build();
        List<ComposeSource> composeSourceList = new ArrayList<>();
        for (int i=0;i<10;i++) {
            ComposeSource composeSource = ComposeSource.builder().bucket(bucket).object("01/temp_"+i).build();
            composeSourceList.add(composeSource);
        }

        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().
                bucket(bucket).
                object("01/01.mp4")
                .sources(composeSourceList)
                .build()
                ;
        minioClient.composeObject(composeObjectArgs);

    }

    //批量清理


    @Test
    public void testSplitString(){
        String str = "hello.mp3.mp3";
        //String str = "小学,初中,高中,大专,本科,研究生,博士";
        String[] buff=str.split("\\.");

        for(int i=0;i<buff.length;i++){
            System.out.println(buff[i]);
        }

//        String str = "小学,初中,高中,大专,本科,研究生,博士";
//        String[] buff = str.split(",");
//        for(int i=0;i<buff.length;i++){
//            System.out.println(buff[i]);
//        }
    }



}

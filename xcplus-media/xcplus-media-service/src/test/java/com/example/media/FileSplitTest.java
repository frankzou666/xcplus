package com.example.media;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

public class FileSplitTest {

    @Test
    public void testSplit() throws Exception {
        File file = new File("/Users/oliver/Downloads/day01.mp4");
        String chunkFilePath = "/Users/oliver/Downloads/";
        int chunkSize = 5 * 1024 * 1024;  //1M
        int chunkNum = (int) Math.ceil(file.length()*1.0 / chunkSize*1.0);
        //使用文件流
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        for (int i = 0; i < chunkNum; i++) {
            File tempFile = new File(chunkFilePath + "temp_" + i);
            RandomAccessFile randomAccessFileTemp = new RandomAccessFile(tempFile, "rw");
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = randomAccessFile.read(buf)) != -1) {
                randomAccessFileTemp.write(buf, 0, length);
                if (randomAccessFileTemp.length() >= chunkSize) {
                    break;
                }
            }
            randomAccessFileTemp.close();
        }
        randomAccessFile.close();
    }

    @Test
    public void testMerge() throws Exception {
        File sourcefile = new File("/Users/oliver/Downloads/day01.mp4");
        String chunkFilePath = "/Users/oliver/Downloads/";
        int chunkSize = 5 * 1024 * 1024;  //1M
        int chunkNum = (int) Math.ceil(sourcefile.length() / chunkSize) + 1;
        //使用文件流
        File targetFile = new File("/Users/oliver/Downloads/target.mp4");
        RandomAccessFile randomAccessFileTarget = new RandomAccessFile(targetFile, "rw");
        for (int i = 0; i < chunkNum; i++) {
            File tempFile = new File(chunkFilePath + "temp_" + i);
            RandomAccessFile randomAccessFileTemp = new RandomAccessFile(tempFile, "rw");
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = randomAccessFileTemp.read(buf)) != -1) {
                randomAccessFileTarget.write(buf, 0, length);
            }
            randomAccessFileTemp.close();
        }
        randomAccessFileTarget.close();
        System.out.println(DigestUtils.md5Hex(new FileInputStream(sourcefile)));
        System.out.println(DigestUtils.md5Hex(new FileInputStream(targetFile)));
    }

    @Test
    public  void test03(){
        System.out.println(Math.ceil((float)2/(float)3));
    }


}

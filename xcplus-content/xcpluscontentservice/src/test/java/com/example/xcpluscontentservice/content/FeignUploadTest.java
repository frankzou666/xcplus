package com.example.xcpluscontentservice.content;


import com.example.xcpluscontentservice.content.config.MultipartSupportConfig;
import com.example.xcpluscontentservice.content.feignclient.MediaFileFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@SpringBootTest
public class FeignUploadTest {

    @Autowired
    MediaFileFeignClient mediaFileFeignClient;
    @Test
    public void test() {
        File file = new File("1.html");
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        mediaFileFeignClient.upload(multipartFile,"course/120.html");

    }


}

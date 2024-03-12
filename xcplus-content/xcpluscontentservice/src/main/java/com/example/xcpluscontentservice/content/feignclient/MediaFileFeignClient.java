package com.example.xcpluscontentservice.content.feignclient;


import com.example.xcpluscontentservice.content.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "media-service",configuration = {MultipartSupportConfig.class})
public interface MediaFileFeignClient {
      @RequestMapping(value = "/media/upload/coursefile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public String upload(@RequestPart("filedata") MultipartFile filedata,
                                      @RequestPart(value="objectName",required = false) String objectName

   );
}

//
//    @RequestMapping(value = "/upload/coursefile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile filedata,
//                                      @RequestPart(value="objectName",required = false) String objectName
//
//    )
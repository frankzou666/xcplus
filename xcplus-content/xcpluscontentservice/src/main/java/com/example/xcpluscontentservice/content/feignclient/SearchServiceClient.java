package com.example.xcpluscontentservice.content.feignclient;

import com.example.search.po.CourseIndex;
import com.example.xcpluscontentservice.content.config.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "search",fallbackFactory = SearchServiceClientFallback.class)
public interface SearchServiceClient {
    @PostMapping(value = "/search/index/course")
    public Boolean add(@RequestBody CourseIndex courseIndex);
}

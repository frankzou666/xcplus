package com.example.xcpluscontentservice.content.feignclient;


import com.example.search.po.CourseIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

//自定义回调工厂需要实现FallbackFactory

@Component
@Slf4j
public class SearchServiceClientFallback implements FallbackFactory<SearchServiceClient> {
    @Override
    public SearchServiceClient create(Throwable cause) {
        return new SearchServiceClient(){

            @Override
            public Boolean add(CourseIndex courseIndex) {
                log.error("添加程索引候发生熔断,索引信息:{}",courseIndex);
                return false;
            };
        };
    }
}



package com.example.system;

import com.example.xcplusbase.base.model.PageParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.system.xuecheng.mapper.DictionaryMapper;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class CourseBaseMapperTests {

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Test
    void testCourseBaseMapper() {

        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(10L);
        System.out.println(dictionaryMapper);



    }
}

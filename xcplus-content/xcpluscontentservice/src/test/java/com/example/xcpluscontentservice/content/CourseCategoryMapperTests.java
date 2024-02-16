package com.example.xcpluscontentservice.content;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.content.model.dto.CourseCategoryTreeDto;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseCategory;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.xcpluscontentservice.content.mapper.CourseCategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseCategoryMapperTests {

    @Autowired
    CourseCategoryMapper CourseCategoryMapper;

    @Test
    void testCourseCategoryMapper() {

        long result = CourseCategoryMapper.selectAllNodes("9900");
        System.out.println(result);



    }
}

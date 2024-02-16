package com.example.xcpluscontentservice.content;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.content.model.dto.CourseCategoryTreeDto;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseBase;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.xcpluscontentservice.content.service.CourseBaseInfoService;
import com.example.xcpluscontentservice.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseCategoryServiceTests {

    @Autowired
    CourseCategoryService courseCategoryService;

    @Test
    void testCourseCategoryService() {

        //查询条件
        List<CourseCategoryTreeDto> courseCategoryTreeDtoList = courseCategoryService.queryTreeNodes("1");
        System.out.println("courseCategoryTreeDtoList");




    }
}

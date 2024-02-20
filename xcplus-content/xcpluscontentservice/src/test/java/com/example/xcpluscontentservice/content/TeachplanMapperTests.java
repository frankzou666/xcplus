package com.example.xcpluscontentservice.content;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.dto.TeachplanDto;
import com.example.content.model.po.CourseBase;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.xcpluscontentservice.content.mapper.CourseBaseMapper;
import com.example.xcpluscontentservice.content.mapper.TeachplanMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootTest
@ComponentScan("xcpluscontentservice.")
public class TeachplanMapperTests {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Test
    void testTeachplanMapper() {
        List<TeachplanDto>  teachplanDtoNodes= teachplanMapper.selectTreeNodes(85L);
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        QueryCourseParamsDto queryCourseParamsDto = new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        queryCourseParamsDto.setAuditStatus("202004");
        queryCourseParamsDto.setPublishStatus("203001");

        PageParams pageParams = new PageParams();
        pageParams.setPageNo(1L);
        pageParams.setPageSize(10L);
        Page<CourseBase> page=new Page<>(pageParams.getPageNo(),pageParams.getPageSize());



    }
}

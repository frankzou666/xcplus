package com.example.content;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.xcpluscontentmodel.content.model.dto.QueryCourseParamsDto;
import com.example.xcpluscontentmodel.content.model.po.CourseBase;
import com.example.xcpluscontentservice.content.mapper.CourseBaseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseBaseMapperTests {

    @Autowired
    CourseBaseMapper courseBaseMapper;

    @Test
    void testCourseBaseMapper() {
        CourseBase courseBase =courseBaseMapper.selectById(24);
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
        Page<CourseBase> result = courseBaseMapper.selectPage(page,queryWrapper);
        List<CourseBase> courBaseList = result.getRecords();
        PageResult<CourseBase> pageResult = new PageResult<>(courBaseList, result.getTotal(), result.getCurrent(), result.getSize());



    }
}

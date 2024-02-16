package com.example.xcpluscontentservice.content;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.content.model.dto.AddCourseDto;
import com.example.xcpluscontentservice.content.service.CourseBaseInfoService;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseBaseInfoServiceTests {

    @Autowired
    CourseBaseInfoService courseBaseInfoService;

    void testCourseBaseInfoService() {

        //查询条件
        QueryCourseParamsDto queryCourseParamsDto = new QueryCourseParamsDto();
        queryCourseParamsDto.setCourseName("java");
        queryCourseParamsDto.setAuditStatus("202004");
        queryCourseParamsDto.setPublishStatus("203001");

        PageParams pageParams = new PageParams();
        pageParams.setPageNo(2L);
        pageParams.setPageSize(10L);
        Page<CourseBase> page=new Page<>(pageParams.getPageNo(),pageParams.getPageSize());

        PageResult<CourseBase> pageResult = courseBaseInfoService.queryCousrBaseList(pageParams,queryCourseParamsDto);
    }

    @Test
    void testCourseBaseInfoServicecreateCourBase() {
        /*
        *
        *   新增课程
        * */

        AddCourseDto  addCourseDto = new AddCourseDto();
        addCourseDto.setName("goodgood");
        addCourseDto.setDescription("Qwrwrwer");
        addCourseDto.setTeachmode("Qwrwrwer");
        addCourseDto.setGrade("204001");
        addCourseDto.setMt("1-3");
        addCourseDto.setSt("1-3-2");
        addCourseDto.setCharge("201001");
        addCourseDto.setPrice(10.1F);
        addCourseDto.setQq("eee");
        addCourseDto.setQq("eee");
        courseBaseInfoService.createCourBase(addCourseDto);




    }
}

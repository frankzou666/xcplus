package com.example.content.api;


import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseBase;


import com.example.xcpluscontentservice.content.service.CourseBaseInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseBaseInfoController {

    private final CourseBaseInfoService courseBaseInfoService;

    public CourseBaseInfoController(CourseBaseInfoService courseBaseInfoService) {
        this.courseBaseInfoService = courseBaseInfoService;
    }
    @RequestMapping("/course/list")
    @ApiOperation("课程查询接口")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false)  QueryCourseParamsDto queryCourseParamsDto){
        PageResult<CourseBase> pageResult=courseBaseInfoService.queryCousrBaseList(pageParams,queryCourseParamsDto);
        return  pageResult;
    }

}

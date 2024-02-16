package com.example.content.api;


import com.example.content.model.dto.AddCourseDto;
import com.example.content.model.dto.CourseBaseInfoDto;
import com.example.content.model.dto.EditCourseDto;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseBase;


import com.example.xcpluscontentservice.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseBaseInfoController {

    private final CourseBaseInfoService courseBaseInfoService;

    public CourseBaseInfoController(CourseBaseInfoService courseBaseInfoService) {
        this.courseBaseInfoService = courseBaseInfoService;
    }
    @RequestMapping("/course/list")
    @ApiOperation("课程分页查询接口")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false)  QueryCourseParamsDto queryCourseParamsDto){
        PageResult<CourseBase> pageResult=courseBaseInfoService.queryCousrBaseList(pageParams,queryCourseParamsDto);
        return  pageResult;
    }

    @ApiOperation("新增课程接口")
    @PostMapping("/course")
    public PageResult<CourseBaseInfoDto> createCourBase(@RequestBody @Validated() AddCourseDto addCourseDto){
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.createCourBase(addCourseDto);
        PageResult<CourseBaseInfoDto> pageResult=new PageResult<CourseBaseInfoDto>();
        return  pageResult;
    }

    @ApiOperation("根据id课程查询接口")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId){
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.getCourseBaseById(courseId);
        return  courseBaseInfoDto;
    }

    @ApiOperation("课程修改接口")
    @PutMapping("/course")
    public CourseBaseInfoDto updateCourseBase(@RequestBody @Validated  EditCourseDto editCourseDto){
        long company_id =1232141425;
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.updateCourseBase(editCourseDto,company_id);
        return  courseBaseInfoDto;
    }

}

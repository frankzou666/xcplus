package com.example.xcpluscontentservice.content.service;

import com.example.content.model.dto.AddCourseDto;
import com.example.content.model.dto.CourseBaseInfoDto;
import com.example.content.model.dto.EditCourseDto;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseBase;


public interface CourseBaseInfoService {
    public PageResult<CourseBase> queryCousrBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /*
    *   返回：新增课程成功的信息
    *
    *
    * */
    public CourseBaseInfoDto createCourBase (AddCourseDto addCourseDto);
    public CourseBaseInfoDto getCourseBaseById (Long courseId);

    public CourseBaseInfoDto updateCourseBase(EditCourseDto editCourseDto,Long company_id);


}

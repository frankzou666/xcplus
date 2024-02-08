package com.example.xcpluscontentservice.content.service;

import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseBase;


public interface CourseBaseInfoService {
    public PageResult<CourseBase> queryCousrBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);
}

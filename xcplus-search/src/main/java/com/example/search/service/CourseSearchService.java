package com.example.search.service;

import com.example.search.dto.SearchPageResultDto;
import com.example.search.po.CourseIndex;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.search.dto.SearchCourseParamDto;

/**
 * @description 课程搜索service
 * @author Mr.M
 * @date 2022/9/24 22:40
 * @version 1.0
 */
public interface CourseSearchService {


    /**
     * @description 搜索课程列表
     * @param pageParams 分页参数
     * @param searchCourseParamDto 搜索条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.search.po.CourseIndex> 课程列表
     * @author Mr.M
     * @date 2022/9/24 22:45
    */
    SearchPageResultDto<CourseIndex> queryCoursePubIndex(PageParams pageParams, SearchCourseParamDto searchCourseParamDto);

 }

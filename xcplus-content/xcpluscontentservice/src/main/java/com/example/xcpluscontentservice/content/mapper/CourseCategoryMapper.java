package com.example.xcpluscontentservice.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.content.model.dto.CourseCategoryTreeDto;
import com.example.content.model.po.CourseCategory;

import java.util.List;


/**
 * <p>
 * 课程分类 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {
    //使用mybatis-plus 来自定义一个方法
    public List<CourseCategoryTreeDto> selectTreeNodes(String id);

    public long selectAllNodes(String id);


}

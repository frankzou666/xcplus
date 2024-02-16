package com.example.content.api;


import com.example.content.model.dto.CourseCategoryTreeDto;
import com.example.content.model.dto.CourseCategoryTreeDto;
import com.example.xcplusbase.base.model.PageResult;
import com.example.xcpluscontentservice.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseCategoryController {
    @Autowired
    CourseCategoryService courseCategoryService;
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes(){
        List<CourseCategoryTreeDto> courseCategoryTreeDtoList = courseCategoryService.queryTreeNodes("1");
        return  courseCategoryTreeDtoList;

    }
}

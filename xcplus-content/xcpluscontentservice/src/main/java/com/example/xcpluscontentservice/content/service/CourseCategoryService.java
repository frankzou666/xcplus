package com.example.xcpluscontentservice.content.service;

import com.example.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategoryService {

    public List<CourseCategoryTreeDto> queryTreeNodes(String id);

}

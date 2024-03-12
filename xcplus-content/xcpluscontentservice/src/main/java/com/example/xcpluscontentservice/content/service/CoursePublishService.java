package com.example.xcpluscontentservice.content.service;

import com.example.content.model.dto.CoursePreviewDto;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


public interface CoursePublishService {

    public CoursePreviewDto getCoursePreviewInfo(Long commponyId,Long courseId);

    public void commitAudit(Long commponyId,Long courseId);

    public void coursePublish(Long commponyId,Long courseId);

    public File generateCourseHtml(Long courseId);

    public void uploadCourseHtml(Long courseId ,File file);
}

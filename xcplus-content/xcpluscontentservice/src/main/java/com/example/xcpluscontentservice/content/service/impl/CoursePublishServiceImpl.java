package com.example.xcpluscontentservice.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.content.model.dto.CourseBaseInfoDto;
import com.example.content.model.dto.CoursePreviewDto;
import com.example.content.model.dto.TeachplanDto;
import com.example.content.model.po.CourseBase;
import com.example.content.model.po.CourseMarket;
import com.example.content.model.po.CoursePublish;
import com.example.content.model.po.CoursePublishPre;
import com.example.messagesdk.service.MqMessageService;
import com.example.xcplusbase.base.exception.XcplusException;
import com.example.xcpluscontentservice.content.config.MultipartSupportConfig;
import com.example.xcpluscontentservice.content.feignclient.MediaFileFeignClient;
import com.example.xcpluscontentservice.content.mapper.CourseBaseMapper;
import com.example.xcpluscontentservice.content.mapper.CourseMarketMapper;
import com.example.xcpluscontentservice.content.mapper.CoursePublishMapper;
import com.example.xcpluscontentservice.content.mapper.CoursePublishPreMapper;
import com.example.xcpluscontentservice.content.service.CourseBaseInfoService;
import com.example.xcpluscontentservice.content.service.CoursePublishService;
import com.example.xcpluscontentservice.content.service.TeachplanService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Service
public class CoursePublishServiceImpl implements CoursePublishService {

    @Autowired
    CourseBaseInfoService courseBaseInfoService ;

    @Autowired
    TeachplanService teachplanService;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    CourseBaseMapper courseBaseMapper;


    @Autowired
    CoursePublishPreMapper coursePublishPreMapper;

    @Autowired
    CoursePublishMapper coursePublishMapper;


    @Autowired
    MqMessageService  mqMessageService;

    @Autowired
    CoursePublishService coursePublishService;

    @Autowired
    MediaFileFeignClient mediaFileFeignClient;



    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long commponyId,Long courseId) {
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();
        //课程基本信息
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.getCourseBaseById(courseId);
        //课程计划信息
        List<TeachplanDto>  teachplans = teachplanService.queryTreeNodes(courseId);


        coursePreviewDto.setCourseBase(courseBaseInfoDto);
        coursePreviewDto.setTeachplans(teachplans);
        return coursePreviewDto;
    }

    @Override
    @Transactional
    public void commitAudit(Long commponyId,Long courseId) {


        //课程信息，课程计划，营销信息保存到预发布表
        CourseBaseInfoDto  courseBaseInfoDto = courseBaseInfoService.getCourseBaseById(courseId);
        //约束前提：比如状态为已提交，等不能再次提交
        if (courseBaseInfoDto==null) {
            XcplusException.cast("课程找不到");
        }
        String auditStatus = courseBaseInfoDto.getAuditStatus();
        if (auditStatus.equals("202003") ){
            XcplusException.cast("课程已提交，请等待申核");
        }
        //查询课程计划
        List<TeachplanDto>  teachplans = teachplanService.queryTreeNodes(courseId);
        if (teachplans.size()==0 || teachplans==null){
            XcplusException.cast("请编写课程计划");
        }

        //组装数据到预发布表，
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseBaseInfoDto,coursePublishPre);

        coursePublishPre.setMtName(courseBaseInfoDto.getMtName());
        coursePublishPre.setStName(courseBaseInfoDto.getStName());

        //营销信息,一个对像如何转转json？,使用ali fastjson
        CourseMarket courseMarket  =courseMarketMapper.selectById(courseId);

        //课程营和计划信息转json
        String courseMarketJson = JSON.toJSONString(courseMarket);
        coursePublishPre.setMarket(courseMarketJson);

        String teachplansJson = JSON.toJSONString(teachplans);
        coursePublishPre.setTeachplan(teachplansJson);

        coursePublishPre.setStatus("202003");
        coursePublishPre.setCreateDate(LocalDateTime.now());

        //查询是否已提交过,如果没有就insert,否则更新
        CoursePublishPre newcoursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (newcoursePublishPre ==null) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            coursePublishPre.setId(courseId);
            coursePublishPreMapper.updateById(coursePublishPre);
        }

        //更新基本课程为已提交
        CourseBase courseBase =courseBaseMapper.selectById(courseId);
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);

    }

    //课程发布
    @Override
    @Transactional
    public void coursePublish(Long commponyId, Long courseId) {

        //从预发布表写到发布表
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre==null) {
            XcplusException.cast("课程没有提交审核 ,课程ID:"+courseId.toString() );
        }
        //如果状态不通过，则异常
        if (!coursePublishPre.getStatus().equals("202004")) {
            XcplusException.cast("课程没有审核审通过不许发布");
        }

        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre,coursePublish);

        //写到发布表中,如果有就更新，否则就insert
        CoursePublish coursePublishObj = coursePublishMapper.selectById(courseId);
        if(coursePublishObj!=null) {
            BeanUtils.copyProperties(coursePublish,coursePublishObj);
            coursePublishMapper.updateById(coursePublishObj);
        } else {
            coursePublishMapper.insert(coursePublish);
        }

        //写任务信息
        mqMessageService.addMessage("course_publish",courseId.toString(), null,null);


        //删除预发布表
        coursePublishPreMapper.deleteById(coursePublishPre.getId());


    }

    @Override
    public File generateCourseHtml(Long courseId) {
        String fileName = courseId+".html";
        File file =null;
        try {
            Configuration configuration = new Configuration();
            //得到当前目录
            String classPath = this.getClass().getResource("/").getPath();
            configuration.setDirectoryForTemplateLoading(new File(classPath+"/templates/"));
            configuration.setDefaultEncoding("utf-8");
            Template template = configuration.getTemplate("course_template.ftl");
            CoursePreviewDto coursePreviewDto = coursePublishService.getCoursePreviewInfo(1L,courseId);
            HashMap<String,Object> map = new HashMap<>();
            map.put("model",coursePreviewDto);

            String htmlStr = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);

            InputStream is =  new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_8));
            FileOutputStream fs = new FileOutputStream(fileName);
            IOUtils.copy(is,fs);
        } catch (Exception ex) {
            log.info("静态化html失败，课程id: {}",courseId.toString(),ex);

        }

        file = new File(fileName);
        return  file;
    }

    @Override
    public void uploadCourseHtml(Long courseId, File file) {
        String objectName = "course/"+courseId+".html";
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        mediaFileFeignClient.upload(multipartFile,objectName);


    }
}

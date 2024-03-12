package com.example.xcpluscontentservice.content.service.jobhandler;


import com.example.content.model.dto.CoursePreviewDto;
import com.example.content.model.po.CoursePublish;
import com.example.messagesdk.model.po.MqMessage;
import com.example.messagesdk.service.MessageProcessAbstract;
import com.example.messagesdk.service.MqMessageService;
import com.example.search.po.CourseIndex;
import com.example.xcplusbase.base.exception.XcplusException;
import com.example.xcpluscontentservice.content.feignclient.SearchServiceClient;
import com.example.xcpluscontentservice.content.mapper.CoursePublishMapper;
import com.example.xcpluscontentservice.content.service.CoursePublishService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

//课程发布任务
@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {

    @Autowired
    MqMessageService mqMessageService;

    @Autowired
    CoursePublishService coursePublishService;

    @Autowired
    SearchServiceClient searchServiceClient;

    @Autowired
    CoursePublishMapper coursePublishMapper;

    //课程发布的job
    @XxlJob("coursePublishJobHandler")
    public void coursePublishJobHandler() {
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        //调用抽像类来执行任务
        process(shardIndex,shardTotal,"course_publish",1,60);


    }
    @Override
    public boolean execute(MqMessage mqMessage) {

        //从MqMessage的类型
        Long courseId = Long.parseLong(mqMessage.getBusinessKey1());

        //写Minio
        generateStaticHtml(mqMessage,courseId);
        //写ES
        saveESIndex(mqMessage,courseId);
        //写redis



        //任务全部正常完成后，返回true
        return true;
    }


    private  void generateStaticHtml(MqMessage mqMessage,Long courseId) {
         Long taskId = mqMessage.getId();
        //如果有了，就不处理
        MqMessageService mqMessageService = this.getMqMessageService();
        int state1 = mqMessageService.getStageOne(taskId);
        if (state1>0) {
            log.info("任务已经处理，无需处理");
            return;
        }
        //生成html文件
        File file = coursePublishService.generateCourseHtml(courseId);

        if (file==null) {
            XcplusException.cast("生成html为空,课程id:"+courseId.toString());
        }
        //写minio
        coursePublishService.uploadCourseHtml(courseId,file);

        //写状态为完成
        mqMessageService.completedStageOne(taskId);

    }

    private  void saveESIndex (MqMessage mqMessage,Long courseId) {
        Long companyId= 1L;
        Long taskId = mqMessage.getId();
        //如果有了，就不处理
        MqMessageService mqMessageService = this.getMqMessageService();
        int state2 = mqMessageService.getStageTwo(taskId);
        if (state2>0) {
            log.info("任务2已经处理，无需处理");
            return;
        }

        //查询课程信息
        //CoursePreviewDto coursePreviewDto =coursePublishService.getCoursePreviewInfo(companyId,courseId);

        CoursePublish coursePublish =coursePublishMapper.selectById(courseId);

        CourseIndex courseIndex = new CourseIndex();
        BeanUtils.copyProperties(coursePublish,courseIndex);
        //远程调用search
        Boolean add = searchServiceClient.add(courseIndex);

        if (!add) {
            XcplusException.cast("远程调用,添加index elasticsearch服务失败,{}:"+courseId);
        }

        //写状态为完成
        mqMessageService.completedStageTwo(taskId);

    }

}

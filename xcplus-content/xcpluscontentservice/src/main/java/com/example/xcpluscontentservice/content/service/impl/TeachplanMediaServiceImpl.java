package com.example.xcpluscontentservice.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.content.model.dto.BindTeachPlanDto;
import com.example.content.model.po.Teachplan;
import com.example.content.model.po.TeachplanMedia;
import com.example.xcpluscontentservice.content.mapper.TeachplanMapper;
import com.example.xcpluscontentservice.content.mapper.TeachplanMediaMapper;
import com.example.xcpluscontentservice.content.service.TeachplanMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Slf4j
public class TeachplanMediaServiceImpl implements TeachplanMediaService {

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Autowired
    TeachplanMapper teachplanMapper;
    @Override
    @Transactional
    public TeachplanMedia bindTeachPlan(BindTeachPlanDto bindTeachPlanDto) {
        //先查询一下，当前的课程是否有绑定;
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<TeachplanMedia>();
        queryWrapper = queryWrapper.eq(TeachplanMedia::getTeachplanId,bindTeachPlanDto.getTeachplanId());
        TeachplanMedia teachplanMedia = teachplanMediaMapper.selectOne(queryWrapper);
        //如果有绑定，需要删除
        if (teachplanMedia != null) {
            teachplanMediaMapper.deleteById(teachplanMedia.getId());
        }
        //绑定；
        Teachplan teachplan = teachplanMapper.selectById(bindTeachPlanDto.getTeachplanId());
        Long courseId = teachplan.getCourseId();
        TeachplanMedia newteachplanMedia = new TeachplanMedia();
        BeanUtils.copyProperties(bindTeachPlanDto,newteachplanMedia);
        newteachplanMedia.setCourseId(courseId);
        newteachplanMedia.setMediaFilename(bindTeachPlanDto.getFileName());
        newteachplanMedia.setMediaId(bindTeachPlanDto.getMediaId().toString());
        newteachplanMedia.setCreateDate(LocalDateTime.now());
        teachplanMediaMapper.insert(newteachplanMedia);
        return newteachplanMedia;
    }
}

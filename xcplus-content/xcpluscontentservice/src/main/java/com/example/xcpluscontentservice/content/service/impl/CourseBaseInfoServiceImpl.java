package com.example.xcpluscontentservice.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.content.model.dto.AddCourseDto;
import com.example.content.model.dto.CourseBaseInfoDto;
import com.example.content.model.dto.EditCourseDto;
import com.example.content.model.po.CourseMarket;
import com.example.xcplusbase.base.exception.RestErrorResponse;
import com.example.xcplusbase.base.exception.XcplusException;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.content.model.dto.QueryCourseParamsDto;
import com.example.content.model.po.CourseBase;
import com.example.xcpluscontentservice.content.mapper.CourseBaseMapper;
import com.example.xcpluscontentservice.content.mapper.CourseCategoryMapper;
import com.example.xcpluscontentservice.content.mapper.CourseMarketMapper;
import com.example.xcpluscontentservice.content.service.CourseBaseInfoService;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
     CourseBaseMapper courseBaseMapper;


    @Autowired
    CourseMarketMapper courseMarketMapper;


    @Autowired
    CourseCategoryMapper courseCategoryMapper;


    @Override
    public PageResult<CourseBase> queryCousrBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto) {
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        queryWrapper.like(StringUtils.isNotEmpty(queryCourseParamsDto.getCourseName()),CourseBase::getName,queryCourseParamsDto.getCourseName());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getAuditStatus()),CourseBase::getAuditStatus,queryCourseParamsDto.getAuditStatus());
        queryWrapper.eq(StringUtils.isNotEmpty(queryCourseParamsDto.getPublishStatus()),CourseBase::getStatus,queryCourseParamsDto.getPublishStatus());



        Page<CourseBase> page=new Page<>(pageParams.getPageNo(),pageParams.getPageSize());
        Page<CourseBase> result = courseBaseMapper.selectPage(page,queryWrapper);
        List<CourseBase> courBaseList = result.getRecords();
        PageResult<CourseBase> pageResult = new PageResult<>(courBaseList, result.getTotal(), result.getCurrent(), result.getSize());
        return  pageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourBase(@RequestBody AddCourseDto addCourseDto) {
        /**
         * 向两张表写数据，如果写数据，都要去要用事务控制
         *
         *
         */

        Long company_id= 1212121L;
        if (StringUtils.isEmpty(addCourseDto.getDescription())){
            XcplusException.cast("对象为空");


        }
        //参数合法性效性，是放在controller,还是在service效验？
        //向course_base写数据,接下用用各种参数，来组装这个对像
        CourseBase courseBase = new CourseBase();
        courseBase.setName(addCourseDto.getName());
        courseBase.setDescription(addCourseDto.getDescription());
        //从原始对像去set对像，这种很烦，使用BeanUtnils.copyProperties，只要属性名一样就可以复制了
        //但是注意如果原始对像为null,目标对像有值，也会overwrite,
        BeanUtils.copyProperties(addCourseDto,courseBase);
        //所以一般都是放到最面，要显式设置放到最后
        courseBase.setCompanyId(company_id);
        courseBase.setCreateDate(LocalDateTime.now());
        courseBase.setAuditStatus("202002");
        courseBase.setStatus("203001");

        int insertRows = courseBaseMapper.insert(courseBase);
        if (insertRows<=0) {
            throw new RuntimeException("插入课程失败");
        }

        //一旦insert成功，就可以得到id
        Long courseId = courseBase.getId();

        //组装课程营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto,courseMarket);
        courseMarket.setId(courseId);
        saveCourseMarket(courseMarket);
        //从数据库中查询返回信息
        CourseBaseInfoDto courseBaseInfoDto = getCourseBaseInfo(courseId);
        return courseBaseInfoDto;
    }

    @Override
    public CourseBaseInfoDto getCourseBaseById(Long courseId) {
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        return courseBaseInfoDto;
    }


    private CourseBaseInfoDto getCourseBaseInfo(long courseId){
        //从课程基本表查询

        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null){
            return  null;
        }

        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        //组装返回数据
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);

        //courseBaseInfoDto.getMtName(courseCategoryMapper.);
        return  courseBaseInfoDto;
    }

    //私有方法，如果有就更新，否则就新增
    private  int saveCourseMarket(CourseMarket courseMarketNew){
        String charge = courseMarketNew.getCharge();
        int insertOrUpdateRows=0;
        if (StringUtils.isEmpty(charge)) {
            throw new RuntimeException("收费规则为空");
        }
        //如果有就更新，否则就增加
        Long id = courseMarketNew.getId();
        CourseMarket courseMarket = courseMarketMapper.selectById(id);
        if (courseMarket == null){
            insertOrUpdateRows = courseMarketMapper.insert(courseMarketNew);

        } else{
            BeanUtils.copyProperties(courseMarketNew,courseMarket);
            courseMarket.setId(courseMarketNew.getId());
            insertOrUpdateRows= courseMarketMapper.updateById(courseMarket);

        }
        return  insertOrUpdateRows;
    }

    @Override
    @Transactional
    public CourseBaseInfoDto updateCourseBase(EditCourseDto editCourseDto, Long company_id) {
        Long courseId = editCourseDto.getId();
        //查询课程是否存在
        //具有业务意义，非一般的业务效验，是写在server层
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if (courseBase == null) {
            XcplusException.cast("课程不存在:"+courseId.toString());
        }
        if (!courseBase.getCompanyId().equals( company_id)){
            XcplusException.cast("只能修改本机构的课程:"+courseId.toString());
        }
        BeanUtils.copyProperties(editCourseDto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        int updateRows = courseBaseMapper.updateById(courseBase);
        if (updateRows<=0) {
            XcplusException.cast("修改课程失败:"+courseId.toString());
        }

        CourseBaseInfoDto courseBaseInfoDto = getCourseBaseInfo(courseId);
        return  courseBaseInfoDto;

    }



}

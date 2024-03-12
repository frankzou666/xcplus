package com.example.xcpluscontentservice.content;

import com.example.content.model.dto.CoursePreviewDto;
import com.example.messagesdk.service.MqMessageService;
import com.example.xcpluscontentservice.content.service.CoursePublishService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;




@SpringBootTest
public class FreeMarkerTest {

    @Autowired
    CoursePublishService coursePublishService;

    @Autowired
    MqMessageService mqMessageService;

    @Test
    public void testFreeMarker() throws Exception{
        Long courseId =1L;
//        //工具类，把页面生成一个字符串，
        Configuration configuration = new Configuration();
         //得到当前目录
        String classPath = this.getClass().getResource("/").getPath();
//        //得到模版目录
      configuration.setDirectoryForTemplateLoading(new File(classPath+"/templates/"));
      configuration.setDefaultEncoding("utf-8");
//        //设置模版
        Template template = configuration.getTemplate("course_template.ftl");
//
         CoursePreviewDto coursePreviewDto = coursePublishService.getCoursePreviewInfo(1L,courseId);
         HashMap<String,Object> map = new HashMap<>();
          map.put("model",coursePreviewDto);

         String htmlStr = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);

//        //使用stream,把字符串写入文件
         InputStream is =  new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_8));
//
//        //输出流
         FileOutputStream fs = new FileOutputStream(courseId+".html");
//
         IOUtils.copy(is,fs);




    }
}

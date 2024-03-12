package com.example.media.service;

import com.example.media.model.po.MediaProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public interface MediaProcessService {


    //得到当前待处理数据库记录
    public List<MediaProcess> getMediaProcess(int shardIndex,int shardTotal);

    public MediaProcess findMediaProcess(long id);

    public void saveMediaProcess(MediaProcess mediaProcess);
}

package com.example.media.jobhandler;

import com.example.media.model.dto.UploadFileParamsDto;
import com.example.media.model.po.MediaProcess;
import com.example.media.service.MediaFileService;
import com.example.media.service.MediaProcessService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;


@Component
public class MedialJob {
    private static Logger logger = LoggerFactory.getLogger(MedialJob.class);

        @Autowired
        MediaProcessService mediaProcessService;

       @Autowired
       MediaFileService mediaFileService;
    @XxlJob("shardingMediaJobHandler")
    public void shardingJobHandler() throws Exception {

        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        long company_id =1232141425;

        logger.info("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

        //得到当前待处理任务文件信息
        List<MediaProcess> mediaProcessList = mediaProcessService.getMediaProcess(shardIndex,shardTotal);
        //如果没有任务处理，直接返回
        if (mediaProcessList.size()<=0) {
            logger.info("当前没有任务处理");
            return;
        }
        //使用线程池来处理
        int processSize = Runtime.getRuntime().availableProcessors();
        CountDownLatch countDownLatch = new CountDownLatch(processSize);
        Executor executor = Executors.newFixedThreadPool(processSize);

        mediaProcessList.forEach(mediaProcess->{
            executor.execute(()->{
                logger.info("开始处理文件,file_id:{}",mediaProcess.getId());
                MediaProcess newMediaProcess = mediaProcessService.findMediaProcess(mediaProcess.getId());

                //这个线程分配到的任务是4才去处理
                if (newMediaProcess.equals("4")) {
                    countDownLatch.countDown();
                    return;
                } else {
                 newMediaProcess.setStatus("4");
                 mediaProcessService.saveMediaProcess(newMediaProcess);
                 try {
                     mediaFileService.downFile(mediaProcess.getFilename(),mediaProcess.getFilePath());
                     //上传文件到minio
                     String  localFilePath = mediaProcess.getFilename();
                     String mimeType = "video/mp4";
                     String bucket = "video";
                     String objectName = getBucketNameFoldPath(mediaProcess.getFileId())+mediaProcess.getFileId()+".mp5";
                     mediaFileService.addMediaFilesToMinIO(localFilePath,mimeType,bucket,objectName);

                 } catch (Exception e){
                     e.printStackTrace();
                     logger.info(e.getMessage());
                     logger.info("文件下载失败,路径:{}",mediaProcess.getFilePath());
                 } finally {
                     countDownLatch.countDown();
                 }

                }
            });





        });

        try {
            countDownLatch.await(30, TimeUnit.MINUTES);
        } catch(Exception e) {

        }

//        //从minio下载文件到本地；
//        for(MediaProcess mediaProcess:mediaProcessList) {
//            MediaProcess newMediaProcess = mediaProcessService.findMediaProcess(mediaProcess.getId());
//            //如果状态等于 4 ，表示有其他进程在处理，否则就我来处理，并把状态更新为 4:处理中
//            //先看看是不是文件是处理中
//            if (newMediaProcess.equals("4")) {
//                break;
//            } else {
//                newMediaProcess.setStatus("4");
//                mediaProcessService.saveMediaProcess(newMediaProcess);
//            }
//            mediaFileService.downFile(mediaProcess.getFilename(),mediaProcess.getFilePath());
//
//            //中间处理
//
//            //上传到minio
//            UploadFileParamsDto  uploadFileParamsDto = new UploadFileParamsDto();
//            uploadFileParamsDto.setFilename(mediaProcess.getFilename());
//            mediaFileService.uploadFile(company_id,uploadFileParamsDto,mediaProcess.getFilename());
//
//            //删除这一条待处理作务信息记录，并且把记录移到对应的历史表中
//
//
//
//        }







//        for (int i = 0; i < shardTotal; i++) {
//            if (i == shardIndex) {
//                logger.info("第 {} 片, 命中分片开始处理", i);
//            } else {
//                logger.info("第 {} 片, 忽略", i);
//            }
//        }

    }

    public String getBucketNameFoldPath(String fileMd5){
        String bucketName = fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/";
        return bucketName;
    }








}

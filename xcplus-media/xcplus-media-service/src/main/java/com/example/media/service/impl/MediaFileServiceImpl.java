package com.example.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.media.mapper.MediaProcessMapper;
import com.example.media.model.po.MediaProcess;
import com.example.xcplusbase.base.exception.XcplusException;
import com.example.xcplusbase.base.model.PageParams;
import com.example.xcplusbase.base.model.PageResult;
import com.example.media.mapper.MediaFilesMapper;
import com.example.media.model.dto.QueryMediaParamsDto;
import com.example.media.model.dto.UploadFileParamsDto;
import com.example.media.model.dto.UploadFileResultDto;
import com.example.media.model.po.MediaFiles;
import com.example.media.service.MediaFileService;
import com.example.xcplusbase.base.model.RestResponse;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.el.parser.BooleanNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/9/10 8:58
 */
@Slf4j
@Service
@Component
public class MediaFileServiceImpl implements MediaFileService {

    @Autowired
    MediaFilesMapper mediaFilesMapper;

    @Autowired
    MediaProcessMapper mediaProcessMapper;

    @Autowired
    MinioClient minioClient;

    @Autowired
    MediaFileService currentProxy;

    //存储普通文件
    @Value("${minio.bucket.files}")
    private String bucket_mediafiles;

    //存储视频
    @Value("${minio.bucket.videofiles}")
    private String bucket_video;

    @Override
    public PageResult<MediaFiles> queryMediaFiels(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {

        //构建查询条件对象
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();

        //分页对象
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<MediaFiles> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<MediaFiles> mediaListResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
        return mediaListResult;

    }

    //根据扩展名获取mimeType
    private String getMimeType(String extension){
        if(extension == null){
            extension = "";
        }
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;

    }

    /**
     * 将文件上传到minio
     * @param localFilePath 文件本地路径
     * @param mimeType 媒体类型
     * @param bucket 桶
     * @param objectName 对象名
     * @return
     */
    public boolean addMediaFilesToMinIO(String localFilePath,String mimeType,String bucket, String objectName){
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)//桶
                    .filename(localFilePath) //指定本地文件路径
                    .object(objectName)//对象名 放在子目录下
                    .contentType(mimeType)//设置媒体文件类型
                    .build();
            //上传文件
            minioClient.uploadObject(uploadObjectArgs);
            log.debug("上传文件到minio成功,bucket:{},objectName:{},错误信息:{}",bucket,objectName);
            return true;
        } catch (Exception e) {
           e.printStackTrace();
           log.error("上传文件出错,bucket:{},objectName:{},错误信息:{}",bucket,objectName,e.getMessage());
        }
        return false;
    }

    //获取文件默认存储目录路径 年/月/日
    private String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/")+"/";
        return folder;
    }
    //获取文件的md5
    private String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5 = DigestUtils.md5Hex(fileInputStream);
            return fileMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath ,  String objectName) {

        //文件名
        String filename = uploadFileParamsDto.getFilename();
        System.out.println(filename);
        //先得到扩展名
        String extension = filename.substring(filename.lastIndexOf("."));

        //得到mimeType
        String mimeType = getMimeType(extension);

        //子目录
        String defaultFolderPath = getDefaultFolderPath();
        //文件的md5值
        String fileMd5 = getFileMd5(new File(localFilePath));
        //如果objectname为空，就使用默认的年月日结构
        if (StringUtils.isEmpty(objectName)) {
            objectName = defaultFolderPath+fileMd5+extension;
        }

        //上传文件到minio
        boolean result = addMediaFilesToMinIO(localFilePath, mimeType, bucket_mediafiles, objectName);
        if(!result){
            XcplusException.cast("上传文件失败");
        }
        //入库文件信息
        MediaFiles mediaFiles = currentProxy.addMediaFilesToDb(companyId, fileMd5, uploadFileParamsDto, bucket_mediafiles, objectName);
        if(mediaFiles==null){
            XcplusException.cast("文件上传后保存信息失败");
        }
        //准备返回的对象
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles,uploadFileResultDto);

        return uploadFileResultDto;
    }


    /**
     * @description 将文件信息添加到文件表
     * @param companyId  机构id
     * @param fileMd5  文件md5值
     * @param uploadFileParamsDto  上传文件的信息
     * @param bucket  桶
     * @param objectName 对象名称
     * @return com.example.media.model.po.MediaFiles
     * @author Mr.M
     * @date 2022/10/12 21:22
     */
    @Transactional
    public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName){
        //将文件信息保存到数据库
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5);
        if(mediaFiles == null){
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(uploadFileParamsDto,mediaFiles);
            //文件id
            mediaFiles.setId(fileMd5);
            //机构id
            mediaFiles.setCompanyId(companyId);
            //桶
            mediaFiles.setBucket(bucket);
            //file_path
            mediaFiles.setFilePath(objectName);
            //file_id
            mediaFiles.setFileId(fileMd5);
            //url
            mediaFiles.setUrl("/"+bucket+"/"+objectName);
            //上传时间
            mediaFiles.setCreateDate(LocalDateTime.now());
            //状态
            mediaFiles.setStatus("1");
            //审核状态
            mediaFiles.setAuditStatus("002003");
            //插入数据库
            int insert = mediaFilesMapper.insert(mediaFiles);
            if(insert<=0){
                log.debug("向数据库保存文件失败,bucket:{},objectName:{}",bucket,objectName);
                return null;
            }
            return mediaFiles;

        }
        return mediaFiles;

    }

    @Override
    public Boolean checkfile(String fileMd5) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {


        //先查数数据库
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(MediaFiles::getFileId,fileMd5);
        MediaFiles mediaFiles = mediaFilesMapper.selectOne(queryWrapper);

        //再查询minio
        if (mediaFiles != null){
            String bucketName = mediaFiles.getBucket();
            String filePath = mediaFiles.getFilePath();
            try {
                InputStream stream =
                        minioClient.getObject(
                                GetObjectArgs
                                        .builder()
                                        .bucket(bucketName)
                                        .object(filePath)
                                        .build());
                if (stream!=null) {
                    return false;
                } else  {
                    return  true;
                }
            } catch(Exception e) {
                e.printStackTrace();
                return  true;
            }

        } else {
            return true;
        }
    }


    //检查文块是否存在
    @Override
    public Boolean checkChunk(String fileMd5, int chunk) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = getBucketNameFoldPath(fileMd5);
        String filePath = bucketName + String.valueOf(chunk);
        try {
            InputStream stream =
                    minioClient.getObject(
                            GetObjectArgs
                                    .builder()
                                    .bucket(bucket_video)
                                    .object(filePath)
                                    .build());
            if (stream==null) {
                return false;
            } else  {
                return  true;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return  false;
        }
    }

    @Override
    public RestResponse uploadchunk(String localFilePath, String fileMd5, int chunk) {
        //上文件分块IO
        String bucketName = getBucketNameFoldPath(fileMd5);
        String filePath = bucketName + String.valueOf(chunk);
        try {
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucket_video)
                            .object(filePath)
                            .filename(localFilePath)
                            .build());
            return  RestResponse.success();
        } catch (Exception e) {
             e.printStackTrace();
             return  RestResponse.validfail(false,"上传文件失败");
        }

    }

    @Override
    public RestResponse mergechunks(String fileMd5, String fileName, int chunkTotal) {
        List<ComposeSource> composeSourceList = new ArrayList<>();
        String bucketNameFoldPath = getBucketNameFoldPath(fileMd5);
        for(int i=0;i<chunkTotal;i++){
            ComposeSource composeSource = ComposeSource.builder().bucket(bucket_video).object(bucketNameFoldPath+i).build();
            composeSourceList.add(composeSource);
        }
        //合并文件
        String filePath= fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+ fileMd5+"."+fileName.split("\\.")[1];
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().
                bucket(bucket_video).
                object(filePath)
                .sources(composeSourceList)
                .build()
                ;
        try {
            minioClient.composeObject(composeObjectArgs);
            Boolean result = addFileInfotoDb(bucket_video,
                           filePath,
                           fileMd5,
                           fileName,
                     chunkTotal*5*1024*104L,
                    "001002",
                           "课程视频"
                       );
           //增加待处理任务的视频信息
            addFileInfotoMediaProcessDb(bucket_video,
                    filePath,
                    fileMd5,
                    fileName
            );

            //写入数据库
            if (result){
                List<DeleteObject> deleteObjectList = new ArrayList<>();
                for(int i=0;i<chunkTotal;i++){
                    DeleteObject deleteObject = new DeleteObject(bucketNameFoldPath+i);
                    deleteObjectList.add(deleteObject);
                }
                //清除chunk
                RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                                .bucket(bucket_video).objects(deleteObjectList).build();
                Iterable<Result<DeleteError>> results =minioClient.removeObjects(removeObjectsArgs);
                for (Result<DeleteError> item : results) {
                    DeleteError error = item.get();
                    System.out.println(
                            "Error in deleting object " + error.objectName() + "; " + error.message());
                }
                return RestResponse.success();
            } else {
                return  RestResponse.validfail("合并失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            return  RestResponse.validfail("合并失败");
        }

    }


    //文件下载
    @Override
    public Boolean downFile(String fileName,String filePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //得到输入流
        InputStream stream =
                minioClient.getObject(
                        GetObjectArgs.builder().bucket(bucket_video).object(filePath).build());

        //创建输出流
        File copied = new File(fileName);
        FileOutputStream fs = new FileOutputStream(copied);
        OutputStream fileOs = new BufferedOutputStream(fs);
        //读写文件
        byte[] buf = new byte[16384];
        int bytesRead;
        while ((bytesRead = stream.read(buf, 0, buf.length)) >= 0) {
            fileOs.write(buf, 0, bytesRead);
            fileOs.flush();
        }
        fs.close();
        return  true;

    }


    @Transactional
    public Boolean addFileInfotoDb(String bucket,
                                   String filePath,
                                   String fileMd5,
                                   String fileName,
                                   Long fileSize,
                                   String fileType,
                                   String tags


    ){
        MediaFiles mediaFiles = new MediaFiles();
        mediaFiles.setBucket(bucket);
        mediaFiles.setFilePath(filePath);
        mediaFiles.setFileId(fileMd5);
        mediaFiles.setFilename(fileName);
        mediaFiles.setFileSize(fileSize);
        mediaFiles.setFileType(fileType);
        mediaFiles.setUrl("/"+bucket+"/"+filePath);
        mediaFiles.setTags(tags);
        mediaFiles.setCreateDate(LocalDateTime.now());
        try {
            mediaFilesMapper.insert(mediaFiles);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;

        }
    }

    @Transactional
    public Boolean addFileInfotoMediaProcessDb(String bucket,
                                   String filePath,
                                   String fileMd5,
                                   String fileName


    ){
        MediaProcess mediaProcess = new MediaProcess();
        mediaProcess.setFileId(fileMd5);
        mediaProcess.setBucket(bucket);
        mediaProcess.setFilePath(filePath);
        mediaProcess.setFilename(fileName);
        //初始化的时候上传成功
        mediaProcess.setStatus("1");
        mediaProcess.setUrl("/"+bucket+"/"+filePath);
        mediaProcess.setCreateDate(LocalDateTime.now());
        try {
            mediaProcessMapper.insert(mediaProcess);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;

        }
    }
    //私有方法，拼接桶名
    public String getBucketNameFoldPath(String fileMd5){
        String bucketName = fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/chunk/";
        return bucketName;
    }

}

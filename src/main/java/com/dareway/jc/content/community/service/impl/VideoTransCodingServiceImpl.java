package com.dareway.jc.content.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dareway.jc.content.community.domain.ConArticle;
import com.dareway.jc.content.community.domain.R;
import com.dareway.jc.content.community.mapper.ConArticleMapper;
import com.dareway.jc.content.community.service.VideoTranscodingService;
import com.dareway.jc.content.community.utils.VideoClipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.UUID;

@Service
public class VideoTransCodingServiceImpl implements VideoTranscodingService {

    @Autowired
    private ConArticleMapper conArticleMapper;

    @Autowired
    private VideoClipUtil videoClipUtil;

    @Async
    @Override
    @Transactional
    public String videoTranscoding480(String videoUrl,Long articleId) throws IOException {
        OutputStream outputStream= null;
        //生成文件的唯一id
        String fileId = UUID.randomUUID().toString().replace("-", "");
        //视频默认封面
        String coverUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbaike.soso.com%2Fp%2F20090710%2F20090710162725-847075167.jpg&refer=http%3A%2F%2Fbaike.soso.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1623030872&t=eab9aef3c6d1bcd9e6976feb6e1d5f6b";
        String fileName = fileId + ".mp4";

        //生成临时文件
        StringBuffer append = new StringBuffer("C:\\Users\\PxWang\\Desktop\\VideoTest\\").append(fileName);

        String localPath = append.toString();
        //保存文件到指定目录
        File inputFile = File.createTempFile(fileId,".mp4");
        inputFile.createNewFile();

        byte[] buffer = new byte[12288];

        try {
            URL url = new URL(videoUrl);
            URLConnection urlConnection = url.openConnection();
            outputStream = new FileOutputStream(inputFile);
            urlConnection.setDoOutput(true);
            InputStream inputStream = urlConnection.getInputStream();

            int len = 0;
            while(-1!=(len=inputStream.read(buffer))){
                outputStream.write(buffer,0,len);
            }
           // VideoClipUtil clipUtil = new VideoClipUtil();

            localPath =  videoClipUtil.transcoding(inputFile,480);
            coverUrl = videoClipUtil.captureImage(inputFile);
            System.out.println(localPath);

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            inputFile.delete();
        }catch (Exception e){
            e.printStackTrace();
        }

        //对数据库原数据进行拼接
        String videoUrls = conArticleMapper.selectVideoUrlsById(articleId);
        if(videoUrls == null){
            videoUrls = "";
            videoUrls = videoUrls.concat("480P:"+localPath);
        }else {
            videoUrls = videoUrls.concat(",480P:"+localPath);
        }
        LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(ConArticle::getVideoUrls,videoUrls)
                .set(ConArticle::getCoverUrl,coverUrl)
                .eq(ConArticle::getId,articleId);
        conArticleMapper.update(null,lambdaUpdateWrapper);

        inputFile.deleteOnExit();
        return localPath;
    }
    @Async
    @Override
    @Transactional
    public String videoTranscoding720(String videoUrl,Long articleId) throws IOException {
        OutputStream outputStream= null;
        //生成文件的唯一id
        String fileId = UUID.randomUUID().toString().replace("-", "");

        String fileName = fileId + ".mp4";

        //生成临时文件
        StringBuffer append = new StringBuffer("C:\\Users\\PxWang\\Desktop\\VideoTest\\").append(fileName);

        String localPath = append.toString();
        //保存文件到指定目录
        File inputFile = File.createTempFile(fileId,".mp4");

        byte[] buffer = new byte[12288];

        try {
            URL url = new URL(videoUrl);
            URLConnection urlConnection = url.openConnection();
            outputStream = new FileOutputStream(inputFile);
            urlConnection.setDoOutput(true);
            InputStream inputStream = urlConnection.getInputStream();

            int len = 0;
            while(-1!=(len=inputStream.read(buffer))){
                outputStream.write(buffer,0,len);
            }


            localPath = videoClipUtil.transcoding(inputFile,720);

            System.out.println(localPath);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            inputFile.delete();
        }catch (Exception e){
            e.printStackTrace();
        }

        String videoUrls = conArticleMapper.selectVideoUrlsById(articleId);
        if(videoUrls == null){
            videoUrls = "";
            videoUrls = videoUrls.concat("720P:"+localPath);
        }else {
            videoUrls = videoUrls.concat(",720P:"+localPath);
        }
        LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(ConArticle::getVideoUrls,videoUrls)
                .eq(ConArticle::getId,articleId);
        conArticleMapper.update(null,lambdaUpdateWrapper);
        inputFile.deleteOnExit();
        return localPath;

    }
    @Async
    @Override
    @Transactional
    public String videoTranscoding1080(String videoUrl,Long articleId) {
//        //生成文件的唯一id
//        String fileId = UUID.randomUUID().toString().replace("-", "");
//        OutputStream outputStream;
//        String fileName = fileId + ".mp4";
//
//        //生成临时文件
//        StringBuffer append = new StringBuffer("C:\\Users\\PxWang\\Desktop\\VideoTest\\").append(fileName);
//
//        String localPath = append.toString();
//        //保存文件到指定目录
//        File inputFile = new File(localPath);
//        inputFile.createNewFile();
////
//        byte[] buffer = new byte[12288];
//
//        try {
//            URL url = new URL(videoUrl);
//            URLConnection urlConnection = url.openConnection();
//            outputStream = new FileOutputStream(inputFile);
//            urlConnection.setDoOutput(true);
//            InputStream inputStream = urlConnection.getInputStream();
//
//            int len = 0;
//            while(-1!=(len=inputStream.read(buffer))){
//                outputStream.write(buffer,0,len);
//            }
//
//            localPath = videoClipUtil.transcoding(fileName,inputFile,1080);
//
//            System.out.println(fileName);
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//            inputFile.delete();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        String videoUrls = conArticleMapper.selectVideoUrlsById(articleId);
        if(videoUrls == null){
            videoUrls = "";
            videoUrls = videoUrls.concat("1080P:"+videoUrl);
        }else {
            videoUrls = videoUrls.concat(",1080P:"+videoUrl);
        }

        LambdaUpdateWrapper<ConArticle> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.set(ConArticle::getVideoUrls,videoUrls)
                .eq(ConArticle::getId,articleId);
        conArticleMapper.update(null,lambdaUpdateWrapper);

        return videoUrl;
    }
}

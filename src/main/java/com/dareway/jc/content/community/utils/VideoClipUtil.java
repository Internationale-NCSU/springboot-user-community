package com.dareway.jc.content.community.utils;

import com.dareway.jc.content.community.feign.FileFeignService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import ws.schild.jave.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 视频转码工具类
 *
 * @author xieshuzhi
 * @Date 2020-04-04 17:18
 *
 * @modified WangPx
 * @Date 2021-4-27
 */
@Component
public class VideoClipUtil {

    @Autowired
    private FileFeignService fileFeignService;

    public  String transcoding(File inputFile,Integer definition) throws IOException {
        long time = System.currentTimeMillis();

        //生成文件的唯一id
        String fileId = UUID.randomUUID().toString().replace("-", "");
        String fileName;
        if(definition==1080) {
             fileName = fileId + "压缩1080";
        }else if(definition==720){
            fileName = fileId + "压缩720";
        }else if(definition==480) {
            fileName = fileId + "压缩480";
        }else {
            return "请返回正确清晰度";
        }
        //生成临时文件
        String finalUrl = "";
        //保存文件到指定目录
        File outputFile = File.createTempFile(fileName,".mp4");

        try {
            //音频文件不做处理
            MultimediaObject object = new MultimediaObject(inputFile);
            AudioInfo audioInfo = object.getInfo().getAudio();
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            if (audioInfo.getBitRate() > 128000) {
                audio.setBitRate(new Integer(128000));
            }
            audio.setChannels(audioInfo.getChannels());
            if (audioInfo.getSamplingRate() > 48050) {
                audio.setSamplingRate(new Integer(48050));
            }
            VideoInfo videoInfo = object.getInfo().getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("h264");

            video.setBitRate(videoInfo.getBitRate());
            //设置码率
            if(definition==480){
                video.setBitRate((int) (videoInfo.getBitRate()*0.3));
            }else if(definition==720){
                video.setBitRate((int)(videoInfo.getBitRate()*0.6));
            }else if(definition==1080&&videoInfo.getBitRate()>6500000) {
                video.setBitRate(6500000);
            }

            video.setFrameRate((int)videoInfo.getFrameRate());
            if(definition==480){
                if (videoInfo.getFrameRate() >= 20) {
                    video.setFrameRate(new Integer(15));
                }
            }else if(definition==720){
                if(videoInfo.getFrameRate()>= 25) {
                    video.setFrameRate(new Integer(20));
                }
            }
            int width = videoInfo.getSize().getWidth();
            int height = videoInfo.getSize().getHeight();

            //视频文件码率转换

            //设置画面大小
//            if(definition==1080){
//                float rat = (float) height/ 1080;
//                video.setSize(new VideoSize((int) (width / rat), 1080));
//            }else if(definition==720) {
//                float rat = (float) height / 720;
//                video.setSize(new VideoSize((int) (width / rat),720 ));
//            }else if(definition==480) {
//                float rat = (float) width / 640;
//                video.setSize(new VideoSize(640, (int)(height/rat) ));
//            }else {
//                return "error";
//            }
            video.setSize(new VideoSize(width,height));
            EncodingAttributes attr = new EncodingAttributes();
            attr.setFormat("mp4");
            attr.setAudioAttributes(audio);
            attr.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(object, outputFile, attr);
            System.out.println("耗时：" + (System.currentTimeMillis() - time) / 1000);
            System.out.println("localPath : "+outputFile.getAbsolutePath());


            FileItem fileItem = createFileItem(outputFile);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            System.out.println(outputFile.getAbsoluteFile());
            finalUrl = (fileFeignService.uploadByFile(multipartFile,"/user/java/unicronaged/dev/content/article/")).getData().getUrl();
            outputFile.deleteOnExit();
            
        } catch (Exception e) {
            e.printStackTrace();
        }


        return finalUrl;
    }
    public static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }

    public String captureImage(File inputFile) throws IOException {

        long times = System.currentTimeMillis();
        //生成文件的唯一id
        String fileId = UUID.randomUUID().toString().replace("-", "");

        String fileName = fileId + ".png";

        //生成临时文件
        StringBuffer append = new StringBuffer("C:\\Users\\PxWang\\Desktop\\VideoTest\\").append(fileName);

        String localPath = append.toString();
        //保存文件到指定目录
        File captureimge = File.createTempFile(fileId,".png");

        MultimediaObject object = new MultimediaObject(inputFile);
        try {
            VideoInfo videoInfo = object.getInfo().getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("png");
            video.setSize(videoInfo.getSize());
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("image2");
            attrs.setOffset(3f);//设置偏移位置，即开始转码位置（3秒）
            attrs.setDuration(0.01f);//设置转码持续时间（1秒）
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(object,captureimge,attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }

        File file = new File(localPath);
        System.out.println("localPath : "+localPath);
        FileItem fileItem = createFileItem(captureimge);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        localPath = (fileFeignService.uploadByFile(multipartFile,"/user/java/unicronaged/dev/content/article/")).getData().getUrl();


        System.out.println("耗时："+(System.currentTimeMillis() - times)/1000);

        captureimge.deleteOnExit();
        return localPath;
    }
}

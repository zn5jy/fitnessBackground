package cn.edu.ustc.fitnessbackground.pojo;

import cn.edu.ustc.fitnessbackground.common.ServiceContext;
import cn.edu.ustc.fitnessbackground.service.VideoService;
import jakarta.websocket.OnMessage;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

/**
 * @ClassName : VideoPOJO  //类名
 * @Description : 处理视频  //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/27  15:51
 */
@ServerEndpoint("/ws/video")
@Component
public class VideoPOJO {
    @OnMessage
    public void onMessage(String jpegData) {
        ServiceContext.getApplicationContext().getBean(VideoService.class).processVideo(jpegData);
    }
}

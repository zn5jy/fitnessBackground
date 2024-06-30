package cn.edu.ustc.fitnessbackground.pojo;


import cn.edu.ustc.fitnessbackground.common.ServerEncoder;
import cn.edu.ustc.fitnessbackground.common.ServiceContext;
import cn.edu.ustc.fitnessbackground.service.VideoService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

/**
 * @ClassName : VideoController  //类名
 * @Description :   //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/27  0:26
 */
@ServerEndpoint(value = "/ws/camera", encoders = {ServerEncoder.class})
@Component
public class CameraPOJO {

    private static Session session;
    private static int frameNumber = 0;


    public static Session getSession(){
        return session;
    }

    public static int getFrameNumber(){
        return frameNumber;
    }

    @OnMessage
    public void onMessage(String jpegData) {
        ServiceContext.getApplicationContext().getBean(VideoService.class).processVideo(jpegData);
        ++frameNumber;
    }

    @OnOpen
    public void onOpen(Session session) {
        CameraPOJO.frameNumber = 0;
        CameraPOJO.session = session;
    }

    @OnClose
    public void onClose() {
        CameraPOJO.frameNumber = 0;
        CameraPOJO.session = null;
    }

}

package cn.edu.ustc.fitnessbackground.configuration;

import cn.edu.ustc.fitnessbackground.util.IO.SingleFrameClient;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : RemoteConfig  //类名
 * @Description : 远程调用python的配置类  //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/27  14:00
 */
@Configuration
public class RemoteConfig {
    @Bean
    public SingleFrameClient singleFrameClient(){
        SingleFrameClient client = new SingleFrameClient();
        client.connect();
        return client;
    }

    @Bean
    public Java2DFrameConverter java2DFrameConverter(){
        return new Java2DFrameConverter();
    }
}

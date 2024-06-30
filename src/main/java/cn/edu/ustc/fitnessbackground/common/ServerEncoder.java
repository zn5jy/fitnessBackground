package cn.edu.ustc.fitnessbackground.common;

import cn.edu.ustc.fitnessbackground.DTO.CameraResponseDTO;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName : ServerEncoder  //类名
 * @Description : 序列化工具类   //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/29  16:21
 */
public class ServerEncoder implements Encoder.Text<CameraResponseDTO> {
    private static final Logger log = LoggerFactory.getLogger(ServerEncoder.class);

    /**
     * 这里的参数 hashMap 要和  Encoder.Text<T>保持一致
     * @param CameraResponseDTO
     * @return
     */
    @Override
    public String encode(CameraResponseDTO cameraResponseDTO) {
        /*
         * 这里是重点，只需要返回Object序列化后的json字符串就行
         * 你也可以使用gosn，fastJson来序列化。
         * 这里我使用fastjson
         */
        try {
            return JSONObject.toJSONString(cameraResponseDTO);
        }catch (Exception e){
            log.error("",e);
        }
        return null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        //可忽略
    }

    @Override
    public void destroy() {
        //可忽略
    }
}

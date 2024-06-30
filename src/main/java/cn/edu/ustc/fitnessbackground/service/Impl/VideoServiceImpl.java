package cn.edu.ustc.fitnessbackground.service.Impl;

import cn.edu.ustc.fitnessbackground.DTO.CameraResponseDTO;
import cn.edu.ustc.fitnessbackground.pojo.CameraPOJO;
import cn.edu.ustc.fitnessbackground.service.VideoService;
import cn.edu.ustc.fitnessbackground.util.IO.Displayer;
import cn.edu.ustc.fitnessbackground.util.IO.SingleFrameClient;
import cn.edu.ustc.fitnessbackground.util.ImageDecoder;
import cn.edu.ustc.fitnessbackground.util.core.BodyModel;
import cn.edu.ustc.fitnessbackground.util.core.Evaluator;
import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;
import cn.edu.ustc.fitnessbackground.util.core.frameRule.FrameResult;
import jakarta.websocket.EncodeException;
import lombok.RequiredArgsConstructor;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;

/**
 * @ClassName : VideoServiceImpl  //类名
 * @Description :   //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/27  14:10
 */
@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final SingleFrameClient client;
    private final int frameExtraction = 5;
    private final Evaluator evaluator;
    private final Java2DFrameConverter converterBI;
    private static LocalTime lasttime;
    private int fps = 0;

    @Override
    public void processVideo(String jpegData) {
        jpegData = jpegData.substring(23);
        Mat mat = ImageDecoder.base642Mat(jpegData);
        Frame frame = new OpenCVFrameConverter.ToIplImage().convert(mat);

        int frameNumber = CameraPOJO.getFrameNumber();
        lasttime = LocalTime.now();

        KeyPoints kp;
        if (frameNumber % frameExtraction == 0) {
            BufferedImage img = converterBI.convert(frame);
            String data = client.detect(img);
            kp = BodyModel.deSerialization(data, frameNumber);
            kp = evaluator.addAndEstimate(kp);
        } else {
            kp = evaluator.estimateAll();
        }

        long milis = Duration.between(lasttime, LocalTime.now()).toMillis();
        lasttime = LocalTime.now();
        if (milis != 0) {
            fps = (int) ((1000 / milis )*0.2 + fps*0.8);
        }
        CameraResponseDTO response = new CameraResponseDTO();
        List<FrameResult> result = evaluator.evaluate(response);
        List<String> watchResult = evaluator.watch();
        Frame f = Displayer.draw(frame, kp, fps, result, watchResult);
        BufferedImage bufferedImage = converterBI.getBufferedImage(f);

        // 将 BufferedImage 转换为字节数组
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            // 将字节数组转换为 base64
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            response.setFrame("data:image/jpeg;base64," + base64Image);

            CameraPOJO.getSession().getBasicRemote().sendObject(response);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }
}

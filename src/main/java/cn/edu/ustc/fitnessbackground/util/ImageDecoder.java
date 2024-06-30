package cn.edu.ustc.fitnessbackground.util;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @ClassName : ImageDecoder  //类名
 * @Description : 视频解码  //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/27  16:25
 */
public class ImageDecoder {
    /**
     * 加载文件 opencv_java490.dll(windows)/libopencv_java401.so文件
     * （windows将文件放到jdk的bin目录下;linux为了简单起见可以放到/usr/lib下面）
     */
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    /**
     * base64转Mat
     *
     * @param jpegData
     * @return
     * @throws IOException
     */
    public static Mat base642Mat(String jpegData) {
        // 对base64进行解码
        byte[] bytes = Base64.getDecoder().decode(jpegData);
        InputStream in = new ByteArrayInputStream(bytes); // 将b作为输入流；
        Mat matImage = null;// CvType.CV_8UC3
        try {
            BufferedImage image = ImageIO.read(in);
            matImage = ImageDecoder.BufImg2Mat(image, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matImage;
    }

    public static Mat BufImg2Mat(BufferedImage original, int imgType, int matType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }

        // Don't convert if it already has correct type
        if (original.getType() != imgType) {

            // Create a buffered image
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);

            // Draw the image onto the new buffer
            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }

        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }

}

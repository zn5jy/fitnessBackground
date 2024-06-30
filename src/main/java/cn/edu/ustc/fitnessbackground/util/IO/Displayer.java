package cn.edu.ustc.fitnessbackground.util.IO;


import cn.edu.ustc.fitnessbackground.util.core.BodyModel;
import cn.edu.ustc.fitnessbackground.util.core.CoreConstant;
import cn.edu.ustc.fitnessbackground.util.core.DisplayConfigConstant;
import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;
import cn.edu.ustc.fitnessbackground.util.core.frameRule.FrameResult;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;

import java.util.List;

public class Displayer {
    private static final int  threshold = 20;
    private static final OpenCVFrameConverter.ToIplImage matConvector = new OpenCVFrameConverter.ToIplImage();

    public static Frame draw(Frame img, KeyPoints kp, int fps, List<FrameResult> results, List<String> watchResult){
        // draw kp

        Mat mat = matConvector.convertToMat(img);
        // b g r t

        int width = img.imageWidth;
        for (int i=0; i<BodyModel.nPoint; i++){
            if (kp.points[i][2] >= threshold){
                Point center = new Point(kp.points[i][0], kp.points[i][1]);
                Scalar cl = kp.isEstimated[i] ? DisplayConfigConstant.colorEstimated :DisplayConfigConstant.colorOK;
                opencv_imgproc.circle(mat, center,DisplayConfigConstant.kpRadius, cl, -1, 8, 0);
                opencv_imgproc.putText(mat, String.valueOf(i), center, 2, DisplayConfigConstant.kpFontSize, cl);
            }
        }
        // draw links
        for (int i=0; i<BodyModel.nConnection; i++){
            int from = BodyModel.connections[i][0];
            int to = BodyModel.connections[i][1];
            if (kp.points[from][2] >= threshold && kp.points[to][2] >= threshold){
                Point startPoint = new Point(kp.points[from][0], kp.points[from][1]);
                Point endPoint = new Point(kp.points[to][0], kp.points[to][1]);
                Scalar cl = kp.isEstimated[from] || kp.isEstimated[to] ? DisplayConfigConstant.colorEstimated : DisplayConfigConstant.colorOK;
                opencv_imgproc.line(mat, startPoint, endPoint, cl, 2, 8, 0);
            }
        }

        // draw FPS
        String s = "FPS:" + fps;
        Point textLeftDown = new Point(img.imageWidth - DisplayConfigConstant.fpsXMargin, DisplayConfigConstant.fpsYMargin);
        opencv_imgproc.putText(mat, s, textLeftDown, 1, DisplayConfigConstant.fpsFontSize, DisplayConfigConstant.colorWrong);

        // draw evaluate results




        // draw frames
        int left = DisplayConfigConstant.textMargin;
        for (int i=0; i<results.size(); i++){
            FrameResult result = results.get(i);
            int up = DisplayConfigConstant.textMargin;
            if(result.isExist){
                String word = "find key frame " + result.name;
                opencv_imgproc.putText(mat, word, new Point(left, up), 2, DisplayConfigConstant.commentFontSize, DisplayConfigConstant.colorOK);
            } else {
                String word = "NOT find frame " + result.name;
                opencv_imgproc.putText(mat, word, new Point(left, up), 2, DisplayConfigConstant.commentFontSize, DisplayConfigConstant.colorEstimated);
            }
            up += DisplayConfigConstant.lineSpacing;
            for (int j=0; j<result.comments.size(); j++){
                String comment = result.comments.get(j);
                int score = result.score.get(j);
                if (score > CoreConstant.ruleScoreThreshold) {
                    opencv_imgproc.putText(mat, comment, new Point(left, up), 2,
                            DisplayConfigConstant.commentFontSize, DisplayConfigConstant.colorOK);
                } else {
                    opencv_imgproc.putText(mat, comment, new Point(left, up), 2,
                            DisplayConfigConstant.commentFontSize, DisplayConfigConstant.colorWrong);
                }
                up += DisplayConfigConstant.lineSpacing;
            }
            left += DisplayConfigConstant.keyFrameTextWidth;
        }

        // draw watch result
        left = DisplayConfigConstant.textMargin;
        int up = DisplayConfigConstant.textMargin + DisplayConfigConstant.lineSpacing * 5;
        for (String watches : watchResult){
            opencv_imgproc.putText(mat, watches, new Point(left, up), 2, DisplayConfigConstant.commentFontSize, DisplayConfigConstant.colorEstimated);
            up += DisplayConfigConstant.lineSpacing;
        }
        return matConvector.convert(mat);
    }
}

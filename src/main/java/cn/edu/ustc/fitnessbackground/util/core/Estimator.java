package cn.edu.ustc.fitnessbackground.util.core;

import java.util.LinkedList;
import java.util.List;
/*
    一个估算器，用于抽帧时根据之前的几帧估计一个中间帧的关键点
    下面的实现方法使用线性回归法，xSize，ySize为窗口长度、待评价指标数，threshold为关键点可见阈值，xs、ys为之前几帧的信息
 */
public class Estimator {
    private final int xSize;
    private final int ySize;
    private final int threshold;
    private LinkedList<Double> xs;
    private LinkedList<Integer>[] ys;

    public Estimator(int xSize, int ySize){
        this.xSize = xSize;
        this.ySize = ySize;
        this.ys = new LinkedList[ySize];
        this.xs = new LinkedList<Double>();
        this.threshold = CoreConstant.kpVisibilityThreshold;
        for (int i=0; i<ySize; i++){
            this.ys[i] = new LinkedList<Integer>();
        }
    }

    public KeyPoints predict(KeyPoints kp){
        int x = kp.frame_number;
        for(int i=0; i<kp.points.length; i++){
            if (kp.points[i][2] < threshold){
                kp.points[i][0] = (int)predictNext(x, i*2);
                kp.points[i][1] = (int)predictNext(x, i*2+1);
                kp.isEstimated[i] = true;
            }
            ys[i*2].addLast(kp.points[i][0]);
            ys[i*2+1].addLast(kp.points[i][1]);
        }
        xs.addLast((double)x);
        if (xs.size() == xSize){
            xs.removeFirst();
            for (LinkedList<Integer> y : ys) {
                y.removeFirst();
            }
        }
        return kp;
    }

    public KeyPoints predictAll(KeyPoints kp){
        for(int i=0; i<kp.points.length; i++){
            kp.points[i][2] = 0;
        }
        int x = kp.frame_number;
        for(int i=0; i<kp.points.length; i++){
            if (kp.points[i][2] < threshold){
                kp.points[i][0] = (int)predictNext(x, i*2);
                kp.points[i][1] = (int)predictNext(x, i*2+1);
                kp.isEstimated[i] = true;
            }
        }
        return kp;
    }
    private double predictNext(double x, int yIndex){
        int len = xs.size();
        if(len == 0){
            return 0;
        }
        double meanx = 0;
        double meany = 0;
        for(var val : xs){
            meanx += val;
        }
        meanx /= len;
        for(var val : ys[yIndex]){
            meany += val;
        }
        meany /= len;
        double numerator = 0;
        double denominator = 0;
        for (int i=0; i<len; i++){
            double deltax = (xs.get(i) - meanx);
            numerator += deltax * (ys[yIndex].get(i) - meany);
            denominator += deltax*deltax;
        }
        double slop;
        if (denominator != 0){
            slop = numerator/denominator;
        } else {
            slop = 0;
        }
        double intercept = meany - slop*meanx;
        return slop*x + intercept;
    }

}

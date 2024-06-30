package cn.edu.ustc.fitnessbackground.util.core;



import cn.edu.ustc.fitnessbackground.DTO.CameraResponseDTO;
import cn.edu.ustc.fitnessbackground.util.core.atomicRule.AtomicRule;
import cn.edu.ustc.fitnessbackground.util.core.frameRule.FrameResult;
import cn.edu.ustc.fitnessbackground.util.core.frameRule.KeyFrame;

import java.util.ArrayList;
import java.util.List;
/*
    评价器，评价的核心。具有一个抽帧预测器estimator和一系列评价关键帧keyFrames, 接收到关键点信息后调用addAndEstimate/estimateAll 估计中间值
    之后调用evaluate 方法，对所有窗口内的关键帧进行检测和评价
 */
public class Evaluator {
    public List<KeyPoints> kps;
    private Estimator estimator;
    private final int threshold;

    public List<KeyFrame> keyFrames;

    public List<AtomicRule> watchList;
    public int windowSize;
    public int curFrame;

    public Evaluator(Estimator estimator){
        this.kps = new ArrayList<KeyPoints>();
        this.estimator = estimator;
        this.threshold = CoreConstant.kpVisibilityThreshold;
        this.keyFrames = new ArrayList<>();
        this.watchList = new ArrayList<>();
        this.curFrame = 0;
    }

    public void addKp(KeyPoints kp){
        kps.add(kp);
    }

    public void setWindowSize(int val){
        if (val > keyFrames.size()){
            val = keyFrames.size();
        }
        this.windowSize = val;
    }

    public KeyPoints addAndEstimate(KeyPoints kp){
        kp = estimator.predict(kp);
        kps.add(kp);
        return kp;
    }

    public void addKeyFrame(KeyFrame kf){
        keyFrames.add(kf);
    }
    public void addWatch(AtomicRule rule) {watchList.add(rule);}
    public List<FrameResult> evaluate(CameraResponseDTO response){
        KeyPoints kp = kps.get(kps.size()-1);
        List<FrameResult> frameResults = new ArrayList<>();
        for (int i=0; i<windowSize; i++){
            int ptr = (curFrame + i) % keyFrames.size();
            System.out.println(ptr);
            FrameResult result = keyFrames.get(ptr).detect(kp, response);
            frameResults.add(result);
        }
        return frameResults;
    }
    public List<String> watch(){
        List<String> watchResult = new ArrayList<>();
        KeyPoints kp = kps.get(kps.size()-1);
        for (AtomicRule rule : watchList){
            double value = rule.calcValue(kp);
            watchResult.add(String.format("Type:%s,kps:%d, %d, %d, val:%.2f ", rule.ruleType,
                    rule.kp1,rule.kp2, rule.kp3, value));
        }
        return watchResult;
    }
    public KeyPoints estimateAll(){
        KeyPoints cp = this.kps.get(this.kps.size()-1).copy();
        cp = estimator.predictAll(cp);
        KeyPoints last = kps.get(kps.size()-1);
        for(int i=0; i<cp.points.length; i++){
            if (last.points[i][2] >= threshold){
                cp.points[i][2] = last.points[i][2]-CoreConstant.estimateConfidenceReduce;
            }
        }
        kps.add(cp);
        return cp;
    }

    public void clearKps(){
        kps.clear();
    }
}

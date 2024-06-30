package cn.edu.ustc.fitnessbackground.configuration;

import cn.edu.ustc.fitnessbackground.util.core.CoreConstant;
import cn.edu.ustc.fitnessbackground.util.core.Estimator;
import cn.edu.ustc.fitnessbackground.util.core.Evaluator;
import cn.edu.ustc.fitnessbackground.util.core.atomicRule.AbsoluteAngle;
import cn.edu.ustc.fitnessbackground.util.core.atomicRule.EvaluateType;
import cn.edu.ustc.fitnessbackground.util.core.atomicRule.RelativeAngle;
import cn.edu.ustc.fitnessbackground.util.core.atomicRule.RelativeDistance;
import cn.edu.ustc.fitnessbackground.util.core.frameRule.DetectRule;
import cn.edu.ustc.fitnessbackground.util.core.frameRule.KeyFrame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : FitnessConfig  //类名
 * @Description : 运动教练的一系列配置，包括关键评价标准  //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/28  20:19
 */
@Configuration
public class FitnessConfig {
    @Bean
    public Evaluator evaluator(){
        Evaluator evaluator = new Evaluator(new Estimator(2, 50));

        // 关键帧1 准备状态
        KeyFrame frameReady = new KeyFrame("ready");
        DetectRule readyExist = new DetectRule("ready", "OK", "Not Found", CoreConstant.ruleExistThreshold);
        // 存在性标准： 左右手与头形成的夹角在40~70 之间
        readyExist.addRule(new RelativeAngle(4, 1, 7, 70, 40, EvaluateType.BT));
        frameReady.addExistRule(readyExist);
        evaluator.addKeyFrame(frameReady);

        // 关键帧2 完成状态
        KeyFrame frameDone = new KeyFrame("Done");
        DetectRule doneExist = new DetectRule("done", "Good", "Not Found", CoreConstant.ruleExistThreshold);
        // 存在性标准： 左右手与头形成的夹角在120~180之间 且 左右手连线高过头顶
        doneExist.addRule(new RelativeAngle(4, 1, 7, 185, 90, EvaluateType.BT));

        // 典型问题1： 高度不足，左右手连线与头部的距离不够远
        frameDone.addExistRule(doneExist);
        DetectRule insufficientMotion = new DetectRule("insufficient motion", "Good!",
                "leve more your arms", CoreConstant.ruleScoreThreshold);
        insufficientMotion.addRule(new RelativeDistance(4, 1, 7, 0, -0.1, EvaluateType.GE));

        // 典型问题2： 不平衡，左右手连线的水平角度超出 +- 7度
        DetectRule unbalance = new DetectRule("Unbalance", "Good!",
                "Keep your arms balance", CoreConstant.ruleScoreThreshold);
        unbalance.addRule(new AbsoluteAngle(4, 7, 7, -7, EvaluateType.BT));

        frameDone.addEvaluateRule(insufficientMotion);
        frameDone.addEvaluateRule(unbalance);
        evaluator.addKeyFrame(frameDone);

        evaluator.addWatch(new RelativeAngle(4, 1, 7, 70, 40, EvaluateType.BT));
        evaluator.addWatch(new RelativeDistance(4, 1, 7, 0, -0.1, EvaluateType.GE));
        evaluator.addWatch(new AbsoluteAngle(4, 7, 7, -7, EvaluateType.BT));
        evaluator.setWindowSize(2);

        return evaluator;
    }
}

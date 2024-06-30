package cn.edu.ustc.fitnessbackground.util.core.atomicRule;

import cn.edu.ustc.fitnessbackground.util.core.CoreConstant;
import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;

/*
    计算kp1 kp2 两点连线的绝对距离， 单位为像素，返回一个double值

 */
public class AbsoluteDistance extends AtomicRule{
    public AbsoluteDistance(int kp1, int kp2, double upper, double lower, EvaluateType evaluateType){
        super(kp1, kp2, 0, upper, lower, AtomicRuleType.AD, evaluateType);
    }

    public double calcValue(KeyPoints kp) {
        var data = kp.points;
        if (data[kp1][2] < CoreConstant.kpVisibilityThreshold || data[kp2][2] < CoreConstant.kpVisibilityThreshold){
            return Double.NaN;
        }
        double dx = data[kp1][0] - data[kp2][0];
        double dy = data[kp1][1] - data[kp2][1];

        return Math.sqrt(dx*dx + dy*dy);
    }
    @Override
    public int detect(KeyPoints kp){
        return super.detectResult(calcValue(kp));
    }
}

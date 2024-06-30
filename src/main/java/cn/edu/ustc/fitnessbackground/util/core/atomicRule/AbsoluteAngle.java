package cn.edu.ustc.fitnessbackground.util.core.atomicRule;


import cn.edu.ustc.fitnessbackground.util.core.CoreConstant;
import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;

/*
    计算kp1 kp2 连线与x轴的夹角，返回一个整数，单位为度
    存在符号， kp1低 kp2 高时为正好 kp2高 kp1低时为负号
 */
public class AbsoluteAngle extends AtomicRule{
    public AbsoluteAngle(int kp1, int kp2, double upper, double lower, EvaluateType evaluateType){
        super(kp1, kp2, 0, upper, lower, AtomicRuleType.AA, evaluateType);
    }
    public double calcValue(KeyPoints kp) {
        var data = kp.points;
        if (data[kp1][2] < CoreConstant.kpVisibilityThreshold || data[kp2][2] < CoreConstant.kpVisibilityThreshold){
            return Double.NaN;
        }
        double dx12 = data[kp2][0] - data[kp1][0];
        double dy12 = data[kp2][1] - data[kp1][1];
        dy12 *= -1;
        if (dx12 == 0){
            if (dy12 > 0) {
                return 90;
            } else if (dy12 < 0){
                return 270;
            } else {
                return 0;
            }
        }
        return Math.atan(dy12/dx12)*180/Math.PI;
    }

    @Override
    public int detect(KeyPoints kp){
        return super.detectResult(calcValue(kp));
    }
}

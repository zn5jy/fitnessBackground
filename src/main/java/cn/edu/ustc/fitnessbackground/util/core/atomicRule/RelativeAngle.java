package cn.edu.ustc.fitnessbackground.util.core.atomicRule;


import cn.edu.ustc.fitnessbackground.util.core.CoreConstant;
import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;

/*
    计算 kp1 kp2 kp3 的夹角 kp2 为中间点，单位为度，返回一个不大于180的整数
    只能是正数
 */
public class RelativeAngle extends AtomicRule{
    public RelativeAngle(int kp1, int kp2, int kp3, double upper, double lower, EvaluateType evaluateType){
        super(kp1, kp2, kp3, upper, lower, AtomicRuleType.RA, evaluateType);
    }
    public double calcValue(KeyPoints kp) {
        var data = kp.points;
        if (data[kp1][2] < CoreConstant.kpVisibilityThreshold || data[kp2][2] < CoreConstant.kpVisibilityThreshold
            || data[kp3][2] < CoreConstant.kpVisibilityThreshold){
            return Double.NaN;
        }
        double dx12 = data[kp2][0] - data[kp1][0];
        double dy12 = data[kp2][1] - data[kp1][1];
        dy12 *= -1;
        double ang1;
        if (dx12 == 0){
            if (dy12 > 0) {
                ang1 =  90;
            } else if (dy12 < 0){
                ang1 =  270;
            } else {
                ang1 =  0;
            }
        } else{
            ang1 = Math.atan(dy12/dx12)*180/Math.PI;
        }
        double dx32 = data[kp2][0] - data[kp3][0];
        double dy32 = data[kp2][1] - data[kp3][1];
        dy32 *= -1;
        double ang2;
        if (dx32 == 0){
            if (dy32 > 0) {
                ang2 =  90;
            } else if (dy32 < 0){
                ang2 =  270;
            } else {
                ang2 =  0;
            }
        } else{
            ang2 = Math.atan(dy32/dx32)*180/Math.PI;
        }
        double ang = Math.abs(180 - ang1 + ang2) % 360;
        if (ang > 180){
            ang = 360 - ang;
        }
        return (int) ang;
    }
    @Override
    public int detect(KeyPoints kp){
        return super.detectResult(calcValue(kp));
    }
}

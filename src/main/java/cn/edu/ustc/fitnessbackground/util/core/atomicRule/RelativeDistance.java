package cn.edu.ustc.fitnessbackground.util.core.atomicRule;


import cn.edu.ustc.fitnessbackground.util.core.CoreConstant;
import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;

/*
    计算kp2 点 到kp1、kp3 连线距离与这个连线长度的比值，返回double 0~100
    这个值有正负，当kp3在下kp1在上， kp2 在kp3 到kp1 连线的右侧时值为负数，左侧时值为正数
 */
public class RelativeDistance extends AtomicRule {
    public RelativeDistance(int kp1, int kp2, int kp3, double upper, double lower, EvaluateType evaluateType){
        super(kp1, kp2, kp3, upper, lower, AtomicRuleType.RD, evaluateType);
    }

    public double calcValue(KeyPoints kp) {
        var data = kp.points;
        if (data[kp1][2] < CoreConstant.kpVisibilityThreshold || data[kp2][2] < CoreConstant.kpVisibilityThreshold
        || data[kp3][2] < CoreConstant.kpVisibilityThreshold){
            return Double.NaN;
        }
        int dx13 = data[kp1][0] - data[kp3][0];
        int dy13 = data[kp1][1] - data[kp3][1];
        if (dx13 == 0 && dy13 == 0){
            // 相对距离无限大
            return 100;
        } else {
            // 为了翻转y轴 这里直接使用负值
            double dx23 = data[kp3][0] - data[kp2][0];
            double dy23 = data[kp3][1] - data[kp2][1];
            // 根据向量叉乘与模长的比计算距离
            return (dx23*dy13 + dx13*dy23) / (dx13*dx13 + dy13*dy13);
        }

    }
    @Override
    public int detect(KeyPoints kp){
        return super.detectResult(calcValue(kp));
    }

}

package cn.edu.ustc.fitnessbackground.util.core.atomicRule;

import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;

/*
    原子规则是以下四种之一
        绝对距离AD：一个点到一个直线的绝对距离，单位为像素
        相对距离RD：一个点到两点连线的举例与这两点连线长度的比，无单位
        绝对倾角AA：一条直线的相对于铅垂线的角度，单位为度
        相对夹角RA：两条直线的夹角，单位为度
    计算出原则规则的评判值之后通过以下三种方式之一进行打分，给出一个上界upper和一个下界lower
        大于：比下界小为0分，大于上界为100分，在这区间按线性取一个整数
        小于：比上界大为0分，比下界小为100分，在这区间按线性取一个整数（无需实现，可以用大于计算然后100减去该值得到）
        之间：中点为100分，边界外为0分，在这区间按线性取一个整数
 */
public abstract class AtomicRule {
    public int kp1;
    public int kp2;
    public int kp3;
    double upper;
    double lower;
    public AtomicRuleType ruleType = null;
    EvaluateType evaluateType = null;
    public AtomicRule(int kp1, int kp2, int kp3, double upper, double lower,
                      AtomicRuleType ruleType, EvaluateType evaluateType){
        this.kp1 = kp1;
        this.kp2 = kp2;
        this.kp3 = kp3;
        this.upper = upper;
        this.lower = lower;
        this.ruleType = ruleType;
        this.evaluateType = evaluateType;
    }
    public abstract int detect(KeyPoints kp);
    protected int detectResult(double value){
        System.out.printf("type:%s kps: %d,%d,%d; val: %.2f, upper: %.2f, lower: %.2f \n",ruleType, kp1, kp2, kp3, value, upper, lower);
        if (Double.isNaN(value)){
            return 0;
        }
        switch (evaluateType){
            case BT -> {
                if(value > upper || value < lower){
                    return 0;
                } else {
                    double mid = (upper + lower)/2;
                    double range = (upper - lower)/2;
                    return 100-(int) (Math.abs(value-mid)/range*100);
                }
            }
            case GE -> {
                if(value > upper){
                    return 100;
                } else if (value < lower){
                    return 0;
                } else {
                    return (int) (Math.abs(value-lower)/(upper-lower)*100);
                }
            }
            default -> {
                if(value > upper){
                    return 0;
                } else if (value < lower){
                    return 100;
                } else {
                    return (int) (Math.abs(upper-value)/(upper-lower)*100);
                }
            }
        }
    }


    public abstract double calcValue(KeyPoints kp);
}

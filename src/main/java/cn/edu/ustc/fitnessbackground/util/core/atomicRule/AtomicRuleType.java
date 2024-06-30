package cn.edu.ustc.fitnessbackground.util.core.atomicRule;

public enum AtomicRuleType {

    AD("AbsoluteDistance"),
    RD("RelativeDistance"),
    AA("AbsoluteAngle"),
    RA("RelativeAngle");

    public final String name;

    private AtomicRuleType(String name){
        this.name = name;
    }
}

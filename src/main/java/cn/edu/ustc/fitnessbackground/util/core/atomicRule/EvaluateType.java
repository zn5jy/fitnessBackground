package cn.edu.ustc.fitnessbackground.util.core.atomicRule;

public enum EvaluateType {
    GE("GreaterThan"),
    LE("LessThan"),
    BT("Between");
    public final String type;

    private EvaluateType(String type){
        this.type = type;
    }

}

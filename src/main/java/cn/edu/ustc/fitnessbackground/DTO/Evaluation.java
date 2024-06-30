package cn.edu.ustc.fitnessbackground.DTO;

import cn.edu.ustc.fitnessbackground.common.ResultType;
import lombok.Data;

/**
 * @ClassName : Evaluation  //类名
 * @Description : 一帧的评价  //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/29  20:48
 */
@Data
public class Evaluation {
    private int actionNumber;
    private String actionName;
    private ResultType resultType;
    private String evaluation;

    public Evaluation(int actionNumber, String actionName, ResultType resultType, String evaluation) {
        this.actionNumber = actionNumber;
        this.actionName = actionName;
        this.resultType = resultType;
        this.evaluation = evaluation;
    }
}

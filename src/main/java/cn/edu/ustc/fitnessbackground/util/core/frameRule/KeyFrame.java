package cn.edu.ustc.fitnessbackground.util.core.frameRule;


import cn.edu.ustc.fitnessbackground.DTO.CameraResponseDTO;
import cn.edu.ustc.fitnessbackground.DTO.Evaluation;
import cn.edu.ustc.fitnessbackground.common.ResultType;
import cn.edu.ustc.fitnessbackground.util.core.CoreConstant;
import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;

import java.util.ArrayList;
import java.util.List;
/*
    表示一个关键帧的检查结果，具有存在性判断规则要求列表existRules、评价性判断规则列表evaluateRules
    对每一帧，调用detect方法，先依照existRules判断是否满足对应动作的条件，再对每个评价指标进行评价，生成一个帧评价结果FrameResult

 */
public class KeyFrame {
    public String name;
    public List<DetectRule> existRules;
    public List<DetectRule> evaluateRules;

    public KeyFrame(String name){
        this.name = name;
        this.existRules = new ArrayList<>();
        this.evaluateRules = new ArrayList<>();
    }

    public void addExistRule(DetectRule rule){
        existRules.add(rule);
    }

    public void addEvaluateRule(DetectRule rule){
        evaluateRules.add(rule);
    }

    public FrameResult detect(KeyPoints kp, CameraResponseDTO response){
        FrameResult frameResult = new FrameResult(existRules, evaluateRules, name);
        // 检查存在性
        int score = 100;
        for (var rule:existRules){
            score = Math.min(rule.detect(kp), score);
            if (score < CoreConstant.ruleExistThreshold) {
                frameResult.isExist = false;
                frameResult.comments.add("frame: "+rule.name+" score:" + score);
                frameResult.score.add(score);
                return frameResult;
            } else {
                frameResult.comments.add("frame: "+rule.name+" score:" + score);
                frameResult.score.add(score);
            }
        }
        int existScore = score;
        // 检查有效性
        for(int i = 0; i < evaluateRules.size(); ++i){
            var rule = evaluateRules.get(i);
            score = rule.detect(kp);
            String comment = String.format("rule:%s, score:%d, ", rule.name, score);
            if (score > CoreConstant.ruleScoreThreshold){
                comment += rule.commentPass;
                response.getResults().add(new Evaluation(i, rule.name, ResultType.PASS, rule.commentPass));
            } else {
                comment += rule.commentFail;
                response.getResults().add(new Evaluation(i, rule.name, ResultType.FAILURE, rule.commentFail));
            }
            frameResult.addComment(comment);
            frameResult.addScore(score);
        }
        return frameResult;
    }
}

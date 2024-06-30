package cn.edu.ustc.fitnessbackground.util.core.frameRule;


import cn.edu.ustc.fitnessbackground.util.core.KeyPoints;
import cn.edu.ustc.fitnessbackground.util.core.atomicRule.AtomicRule;

import java.util.ArrayList;
import java.util.List;

/*
    一个评价标准，可以是存在性标准也可以是评价性标准，对于评价性标准，如果不满足条件会提示错误信息和改善建议
    一个标准由多个原子规则组成，它们之间可能是与、或的关系(isAnd决定)，通过权重给出一个整数0~99的符合性打分。
    运行detect 返回一个评价标准
 */
public class DetectRule {
    public String name;
    public List<AtomicRule> rules;
    public boolean isAnd; // 默认是与的关系
    public String commentPass;
    public String commentFail;

    private int threshold;

    public DetectRule(String name, String commentPass, String commentFail, int threshold){
        this.name = name;
        this.commentPass = commentPass;
        this.commentFail = commentFail;
        this.rules = new ArrayList<AtomicRule>();
        this.isAnd = true;
        this.threshold = threshold;
    }

    public void addRule(AtomicRule rule){
        rules.add(rule);
    }

    public int detect(KeyPoints kp){
        int score = 100;
        if (isAnd){
            for(var rule : rules){
                int cur = rule.detect(kp);
                if (cur < threshold){
                    score = cur;
                    break;
                } else {
                    score = Math.min(score, cur);
                }
            }
        } else {
            score = 0;
            for(var rule: rules){
                int cur = rule.detect(kp);
                score = Math.max(score, cur);
            }
        }
        return score;
    }


}

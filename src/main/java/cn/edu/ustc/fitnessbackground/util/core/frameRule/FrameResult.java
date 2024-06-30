package cn.edu.ustc.fitnessbackground.util.core.frameRule;

import java.util.ArrayList;
import java.util.List;
/*
    对一个帧的一个frame评价结果，用于传递给Display 将评价结果可视化
 */
public class FrameResult {
    public List<DetectRule> existRules;
    public List<DetectRule> evaluateRules;
    public boolean isExist;
    public List<String> comments;
    public List<Integer> score;

    public String name;

    public FrameResult(List<DetectRule> existRules, List<DetectRule> evaluateRules, String name){
        this.existRules = existRules;
        this.evaluateRules = evaluateRules;
        this.comments = new ArrayList<String>();
        this.isExist = true;
        this.name = name;
        this.score = new ArrayList<>();
    }

    public void addComment(String comment){
        this.comments.add(comment);
    }
    public void addScore(int val){score.add(val);}
}


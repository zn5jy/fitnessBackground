package cn.edu.ustc.fitnessbackground.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : CamareResponseDTO  //类名
 * @Description :   //描述
 * @Author : 31524 //作者
 * @Date: 2024/6/29  16:08
 */
@Data
public class CameraResponseDTO {
    private String frame;
    private List<Evaluation> results;

    public CameraResponseDTO(){
        this.results = new ArrayList<>();
    }
}

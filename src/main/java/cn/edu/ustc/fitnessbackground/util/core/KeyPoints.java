package cn.edu.ustc.fitnessbackground.util.core;

public class KeyPoints {
    public int frame_number;
    public int[][] points;
    public boolean[] isEstimated;

    public  KeyPoints copy(){
        KeyPoints cp = new KeyPoints();
        cp.frame_number = frame_number+1;
        cp.points = new int[points.length][3];
        cp.isEstimated = new boolean[points.length];
        for(int i=0; i<points.length; i++){
            System.arraycopy(points[i], 0, cp.points[i], 0, points[0].length);
        }
        return cp;
    }
}

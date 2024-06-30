package cn.edu.ustc.fitnessbackground.util.core;



public class BodyModel {
    public static final String name = "25kps";
    public static final int nPoint = 25;
    public static final int[][] connections = new int[][]{
            {17,15},{0,15},{0,16},{16,18},{0,1},
            {2,1},{1,5},{2,3},{3,4},{5,6},{6,7},
            {1,8},{8,9},{8,12},{10,9},{12,13},{10, 11},
            {11,24},{11,22},{22,23},{13,14},
            {14,21},{14,19},{19,20}};
    public static final int nConnection = connections.length;
    private static final int threshold = CoreConstant.kpVisibilityThreshold;

    public static KeyPoints deSerialization(String data, int frame_number){
        KeyPoints kp = new KeyPoints();
        kp.frame_number = frame_number;
        kp.points = new int[nPoint][3];
        kp.isEstimated = new boolean[nPoint];
        if (data.length() >= nPoint *10) {
            for (int i = 0; i < nPoint; i++) {
                kp.points[i][0] = Integer.parseInt(data.substring(i * 10, i * 10 + 4));
                kp.points[i][1] = Integer.parseInt(data.substring(i * 10 + 4, i * 10 + 8));
                kp.points[i][2] = Integer.parseInt(data.substring(i * 10 + 8, i * 10 + 10));
                if(kp.points[i][2] > threshold){
                    kp.isEstimated[i] = false;
                }
            }
        }
        return kp;
    }

}

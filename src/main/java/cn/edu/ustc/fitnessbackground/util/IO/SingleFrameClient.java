package cn.edu.ustc.fitnessbackground.util.IO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class SingleFrameClient {
    private String host;
    private int port;
    private int frame_count;
    private Socket socket;
    private DataOutputStream os;
    private ByteArrayOutputStream baos;
    private PrintWriter pw;
    private InputStream is;
    private BufferedReader br;


    public SingleFrameClient(){
        this.host = "127.0.0.1";
        this.port = 9999;
        this.frame_count = 0;
    }
    public SingleFrameClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public boolean connect(){
        try {
            socket = new Socket(this.host, this.port);
            os = new DataOutputStream(socket.getOutputStream());
            baos = new ByteArrayOutputStream();
            pw = new PrintWriter(os);
            is = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String detect(BufferedImage img){
        if (img == null || socket == null){
            return "";
        }
        String readline = "";
        try {
            ImageIO.write(img,"jpg", baos);
            // send size of img
            var data = Base64.getEncoder().encodeToString(baos.toByteArray());
            os.writeInt(data.length());
            // send img data
            pw.print(data);
            pw.flush();
            baos.reset();

            // wait result
            readline = br.readLine();
            frame_count ++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readline;
    }
    public void close(){
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

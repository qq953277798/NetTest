package Test1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Max on 2018/4/12.
 */
public class OutThread extends Thread{
    Socket s ;

    public OutThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try (OutputStream os = s.getOutputStream();
             DataOutputStream dos = new DataOutputStream(os)){
            Scanner in = new Scanner(System.in);
            String s = "";
            while (!s.equals("exit")) {
                s = in.nextLine();
                dos.writeUTF(s);
                System.err.println("发送消息:"+s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

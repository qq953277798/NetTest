package Test1;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Max on 2018/4/12.
 */
public class InputThread extends Thread{
    Socket s;

    public InputThread(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try (InputStream is = s.getInputStream();
             DataInputStream dis = new DataInputStream(is)){
            String s = "";
            while (!s.equals("exit")) {
                s = dis.readUTF();
                char[] cs = s.toCharArray();
                StringBuilder sb = new StringBuilder();
                for (char a : cs) {
                    if (Character.isAlphabetic(a)){
                        sb.append(Character.toUpperCase(a));
                        continue;
                    }
                    sb.append(a);
                }
                System.out.println("接受消息:"+sb.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

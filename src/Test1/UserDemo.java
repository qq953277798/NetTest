package Test1;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Max on 2018/4/12.
 */
public class UserDemo {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);

                System.out.println("连接成功------exit退出");
                new InputThread(socket).start();
                new OutThread(socket).start();


        } catch (IOException e) {
            System.out.println("没有服务器在线");
        }

    }
}

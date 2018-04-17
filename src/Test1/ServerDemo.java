package Test1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Max on 2018/4/12.
 */
public class ServerDemo {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("服务器创建成功");
            Socket socket = serverSocket.accept();
            if (socket != null) {
                System.out.println("连接成功------exit退出");
                new InputThread(socket).start();
                new OutThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

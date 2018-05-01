package Test2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.TreeMap;

import static Test2.Server.showTable;
import static Test2.Server.update;

public class User {
     static Socket soket = null;
     static TreeMap<String, Value> table = null;//路由器表
    public static String name = "b";
    public static void main(String[] args) {
        init();
        send(soket, table, name);//将表放入流中
//        while(true) {
//            TreeMap<String, Value> nextTable = Server.reception(soket);//接收路由表
//            //更新
//            boolean flog = update(table, nextTable);
//
//            if (!flog) {//没发生更新
//                break;
//            }
//            else{ //发送新表
//                System.out.println("User更新后");
//                showTable(table);
//                User.send(soket, table, name);
//            }
//
//        }
        try {
            soket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(Socket soket, TreeMap<String, Value> table,String tump) {
        try {
            OutputStream outputStream = soket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            Set<String> key = table.keySet();
            for (String k:key) {
                dataOutputStream.writeUTF(k);
                dataOutputStream.writeInt(table.get(k).tumpNum+1);
                dataOutputStream.writeUTF(tump);
                dataOutputStream.flush();
            }
            dataOutputStream.writeUTF("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        //开启服务器
        try {
            Socket soket = new Socket("127.0.0.1", 9999);
            User.soket = soket;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接服务器失败");
            System.exit(0);
        }
        //从文件中读取路由表  名字  跳数 下一跳
        TreeMap<String, Value> table = new TreeMap<>();
        File file = new File("D:\\JavaProject\\NetTest\\src\\Test2\\userTable.txt");
        if (!file.exists()) {
            System.out.println("文件不存在,请检查后重试");
            System.exit(0);
        }
        try (
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);)
        {
            String line = "";
            while((line= bufferedReader.readLine())!=null){
                String[] strings = line.split(",");
                table.put(strings[0], new Value(Integer.parseInt(strings[1]), strings[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对表更新
        User.table = table;
        //显示路由表
        showTable(table);
    }


}

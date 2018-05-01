package Test2;

import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.TreeMap;

/**
 * 实现rip路由更新协议
 * 思路:
 * 1.初始化 建立连接 从文件中将路由表读取 存入路由表中(TreeMap)
 * 2.从流中取出路由表
 * 3.更新
 * 3.1 表没有发生更新 结束
 * 4.将新的表放入流中
 */
public class Server{
     static ServerSocket serverSocket = null;//服务器
     static TreeMap<String, Value> table = null;//路由器表
    public static String name  = "a";
    public static void main(String[] args) {
        init();
        Socket user = null;
        try {
            user = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("已检测到用户"+user.getLocalAddress());
            //while(true) {
                TreeMap<String, Value> nextTable = reception(user);//接收路由表
                //更新
                boolean flog = update(table, nextTable);
//                if (!flog) {//没发生更新
//                    break;
//                }
//                else{
                    System.out.println("Server更新后");
                    showTable(table);
//                    User.send(user, table, name);
//                }
           // }
        try {
            user.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean update(TreeMap<String, Value> table, TreeMap<String, Value> nextTable) {
        /**
         * 1.扫描相邻节点的目的地是否在本表中  
         *  1.1 不在 直接添加
         *  1.2 在  查看下一跳是否相同
         *   1.2.1 相同 直接更新
         *   1.2.2 不同 更新为小的
         */
        boolean result  = false;//没发生改变
        //扫描相邻表
        Set<String> keys = nextTable.keySet();
        for (String k:keys) {
            if (table.containsKey(k)){//在本表中
                if (table.get(k).tumpItem.equals(nextTable.get(k).tumpItem)) {//下一跳相同 无条件更新
                    int num = nextTable.get(k).tumpNum;
                    String next = nextTable.get(k).tumpItem;
                    table.put(k, new Value(num,next));
                    result = true;//发生改变
                }
                else{ //下一跳不同
                    if (table.get(k).tumpNum>nextTable.get(k).tumpNum){//新表跳数小 更新table
                        int num = nextTable.get(k).tumpNum;
                        String next = nextTable.get(k).tumpItem;
                        table.put(k, new Value(num,next));
                        result = true;//发生改变
                    }
                }
            }
            else{//本表中没有 直接添加
                int num = nextTable.get(k).tumpNum;
                String next = nextTable.get(k).tumpItem;
                table.put(k, new Value(num,next));
                result = true;//发生改变
            }
        }
        return result;
    }

    public static TreeMap<String,Value> reception(Socket user) {
        TreeMap<String, Value> result = new TreeMap<>();
        try(InputStream inputStream = user.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);)
        {
            while(true){
                String key = dataInputStream.readUTF();
                if (key.equals("end")) {
                    break;
                }
                result.put(key, new Value(dataInputStream.readInt(), dataInputStream.readUTF()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public static void init() {
        //开启服务器
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            Server.serverSocket = serverSocket;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器开启失败");
            System.exit(0);
        }
        //从文件中读取路由表  名字  跳数 下一跳
        TreeMap<String, Value> table = new TreeMap<>();
        File file = new File("D:\\JavaProject\\NetTest\\src\\Test2\\serverTable.txt");
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
        Server.table = table;
        //显示路由表
        showTable(table);
    }

    public static void showTable(TreeMap<String, Value> table) {
        Set<String> key = table.keySet();
        System.out.println("----- table ------");
        System.out.printf("|%-7s|%-4s|%-4s|\n", "address", "num", "next");
        for (String k:key) {
            System.out.printf("|%-7s|%-4d|%-4s|\n",k,table.get(k).tumpNum,table.get(k).tumpItem);
        }
        System.out.println("------------------");
    }

}



import java.util.Base64;
import java.util.Date;

import orbyun.vtm.monitor.databus.Sender;

public class T1 {
    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Sender sender = new Sender();
                String tmp = "wangxiongfeng|00|{'SN':'wangxiongfeng ','type':'00','mac':'8C-EC-4B-5F-EB-08','ip':'192.168.0.81','serialNumber':'Z9AQ8YXY','cpu':'8.6%','menTotal':'17037156352','menUsed':'3498684416','version':'V1.0.1215'}";
                try {
                    sender.sendMsg(tmp);
                    System.out.println("  已发生心跳到服务端" + tmp);
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}




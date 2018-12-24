package sample;

import com.oceanos.ros.core.connections.UDPServer;

import java.net.SocketException;
import java.net.UnknownHostException;

public class SampleDataServer {
    public static void main(String[] args) {

            try {
                UDPServer server = new UDPServer(4447);
                server.start();

                new Thread(()->{
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < 100; i++) {
                        String data = (i*1000+","+Math.random()*100+","+Math.random()*80);
                        server.sendData(data.getBytes());
                        System.out.println(data);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }





    }
}

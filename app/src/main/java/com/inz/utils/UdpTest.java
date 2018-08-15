package com.inz.utils;

import com.inz.action.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpTest {

    public static void sendTest(final byte []msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket(6234);
                    InetAddress serverAddress = InetAddress.getByName("192.168.18.156");
                    byte data[] = "hello world".getBytes();
                    DatagramPacket packet = new DatagramPacket(msg,msg.length,serverAddress,6234);
                    socket.send(packet);
                    socket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }

}

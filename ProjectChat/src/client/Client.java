package client;

import java.io.*;
import java.net.*;

public class Client {

    final static int ServerPort = 3000;
    private static final String ServerHost = null;
    private static Thread t1;

    public static void main(String args[]){
        try {
            // establish the connection
            Socket s = new Socket(InetAddress.getLocalHost(), ServerPort);
            t1 = new Thread(new Connexion(s));
            t1.start();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

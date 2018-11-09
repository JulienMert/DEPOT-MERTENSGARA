package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket s;
    private ServerSocket ss;
    public static Thread t1;
    protected ArrayList<ClientConnexion> clientList;
    private ArrayList<Account> accountList;
    private Database db;

    private ClientConnexion clientConnexion;

    //choix du port server
    public static final int SERVER_PORT = 3000;

    public ClientHandler(ArrayList<Account> accountList, Database db) throws IOException {

        this.ss = new ServerSocket(SERVER_PORT);
        System.out.println("Serveur à l'écoute sur le port " + ss.getLocalPort());
        this.clientList = new ArrayList<ClientConnexion>();
        this.accountList = accountList;
        this.db = db;
    }

    public void run()  {

        while (true) {
            System.out.println("Attente de connexion client...");

            try {
                s = this.ss.accept();
                clientConnexion = new ClientConnexion(s);
                clientList.add(clientConnexion);
                System.out.println("Nouveau client connecté");
                System.out.println("Nombre de clients connectés : "+clientList.size());
                t1 = new Thread(new ClientAuthentification(clientList, accountList, s, db));
                t1.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

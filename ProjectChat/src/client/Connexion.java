package client;

import server.Account;
import server.Topic;
import shared.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Connexion implements Runnable{

    private Socket s;
    private Scanner sc;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean auth = false;
    private boolean accountcreated = false;
    public static Thread emisison;
    public static Thread reception;
    private ArrayList<Topic> ListeTopic;
    private int choice;


    public Connexion(Socket s) throws IOException {
        //flux i/o
        this.s = s;
        out = new ObjectOutputStream(s.getOutputStream());
        in = new ObjectInputStream(s.getInputStream());
    }

    public void run(){

        do{
            sc = new Scanner(System.in);
            System.out.println("Connexion au server OK");
            System.out.println("1 : Connexion");
            System.out.println("2 : Creer nouveau compte");
            System.out.println("Votre choix : ");
            choice = sc.nextInt();
            choice = Integer.valueOf(choice);
        }
        while(choice !=1 && choice !=2);

        if (choice == 1)
        {
            try {
                out.writeObject(Request.AUTHENTIFICATION);
                do{
                System.out.println("Login");
                String login = sc.next();
                System.out.println("Password");
                String password = sc.next();
                Account account = new Account(login, password);
                out.writeObject(account);

                Request request = (Request) in.readObject();

                if (request.equals(Request.AUTHENTIFICATIONRIGHT)) {
                    System.out.println("Authentification réussie");
                    auth = true;

                    //selection du topic
                     request = (Request) in.readObject();
                    if (request.equals(Request.TOPICSELECTION)) {
                        //on recupere la liste de topics
                        ListeTopic = (ArrayList<Topic>) in.readObject();

                        System.out.println("Les topics suivants sont accessible :");
                        for(int i = 0; i<ListeTopic.size(); i++)
                        {
                            System.out.println((i+1) +": " + ListeTopic.get(i).getTopicName());
                        }
                        System.out.println("Veuillez selectionner un topic :");
                        int choiceTopic = sc.nextInt();
                        //on envoie num du topic choiceTopic au client
                        out.writeObject(choiceTopic);
                        System.out.println("Bienvenue dans le " +ListeTopic.get(choiceTopic-1).getTopicName());
                    }

                    //Demarrage du chat
                    //Thread émission
                    emisison = new Thread(new Emission(out));
                    //Thread réception
                    reception = new Thread(new Reception(in));
                    //start des 2 threads
                    emisison.start();
                    reception.start();


                } else if (request.equals(Request.AUTHENTICATIONFAIL)) {
                    System.out.println("Login ou password incorrect");
                }
                }while(!auth);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        else if (choice == 2)
        {
            try {
                out.writeObject(Request.CREATEACCOUNT);
do {
    System.out.println("Entrer un login");
    String login = sc.next();
    System.out.println("Entrer un password");
    String pass = sc.next();

    Account account = new Account(login, pass);
    out.writeObject(account);

    //lecture reponse du serveur
    Request request = (Request) in.readObject();
    if (request.equals(Request.CREATEACCOUNTSUCCES)) {
        System.out.println("Création de compte OK");
        accountcreated = true;
    } else if (request.equals(Request.CREATEACCOUNTFAIL)) {
        System.out.println("Login déjà utilisé ! Veuillez recommencer");
    }
}while(!accountcreated);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

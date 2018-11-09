package server;

import shared.Request;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientAuthentification implements Runnable{

    //private String login;
    //private String password;
    //private Thread t1;
    public boolean accountcreated = false;
    private ArrayList<Account> accountList;
    private ArrayList<ClientConnexion> clientList;
    private int ClientID;
    private Socket socket;
    private Account account;
    private Database db;
    public static Thread discussion;
    private ArrayList<Topic> listeTopic;
    private Topic topic1, topic2, topic3;


    public ClientAuthentification(ArrayList<ClientConnexion> clientList, ArrayList<Account> accountList, Socket socket, Database db) {
        this.accountList = accountList;
        this.clientList = clientList;
        this.socket = socket;
        this.db = db;
    }

    public void run() {

        boolean auth=false;
        listeTopic = new ArrayList<Topic>();
        try {
            accountList = db.loadAccount();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for(int i =0; i<clientList.size(); i++) {
            if (socket == clientList.get(i).getS()) {
                ClientID = i;
            }
        }
            try {
                //recuperation de la demande du client
                Request request = (Request) clientList.get(ClientID).getIn().readObject();

                if (request.equals(Request.CREATEACCOUNT)) {
                    do {
                        this.account = (Account) clientList.get(ClientID).getIn().readObject();

                        if (accountAvailable(account.getLogin(), accountList)) {
                            clientList.get(ClientID).getOut().writeObject(Request.CREATEACCOUNTSUCCES);
                            accountList.add(account);
                            //une fois que le compte est validé on le sauve dans la bdd
                            db.saveAccount(accountList);
                            accountcreated = true;
                        } else {
                            clientList.get(ClientID).getOut().writeObject(Request.CREATEACCOUNTFAIL);
                        }
                    } while (!accountcreated);
                }
                else if (request.equals(Request.AUTHENTIFICATION))
                {
                    do {
                        this.account = (Account) clientList.get(ClientID).getIn().readObject();

                        if (accountOk(account.getLogin(), account.getPassword() ,accountList)) {
                            clientList.get(ClientID).getOut().writeObject(Request.AUTHENTIFICATIONRIGHT);
                            accountList.add(account);
                            db.saveAccount(accountList);
                            auth = true;

                            //Création des topics manuelle
                            topic1 = new Topic ("Topic1", 1);
                            topic2 = new Topic ("Topic2", 2);
                            topic3 = new Topic ("Topic3", 3);

                            listeTopic.add(topic1);
                            listeTopic.add(topic2);
                            listeTopic.add(topic3);

                            clientList.get(ClientID).getOut().writeObject(Request.TOPICSELECTION);
                            clientList.get(ClientID).getOut().writeObject(listeTopic);
                            clientList.get(ClientID).getOut().flush();

                            //récupération du choix du topic
                            int choice = (int)clientList.get(ClientID).getIn().readObject();

                            //on assigne numero de topic au client authentifié
                            clientList.get(ClientID).setTopicID(choice);
                            discussion = new Thread (new ChatHandler(clientList.get(ClientID).getOut(), clientList.get(ClientID).getIn(), clientList, ClientID));
                            discussion.start();
                        } else {
                            clientList.get(ClientID).getOut().writeObject(Request.AUTHENTICATIONFAIL);
                        }
                    } while (!auth);
                }
                } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    //boolean compte bien crée
    public boolean accountOk (String login, String pass, ArrayList<Account> accountList)
    {
        boolean flag = false;

        for(int i=0; i<accountList.size(); i++)
        {
            if(login.equals(accountList.get(i).getLogin()) && pass.equals(accountList.get(i).getPassword()))
            {
                flag = true;
            }
        }
        return flag;
    }

    //boolean compte disponible
    public boolean accountAvailable (String login, ArrayList<Account> accountList)
    {
        boolean flag = true;

        for(int i=0; i<accountList.size(); i++)
        {
            if(login.equals(accountList.get(i).getLogin()))
            {
                flag = false;
            }
        }
        return flag;
    }
}

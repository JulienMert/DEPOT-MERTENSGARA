package server;

import shared.Request;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ChatHandler implements Runnable {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ArrayList<ClientConnexion> clientList;
    private int ClientID;

    public ChatHandler(ObjectOutputStream out, ObjectInputStream in, ArrayList<ClientConnexion> clientList, int ClientID)
    {
        this.out = out;
        this.in = in;
        this.clientList = clientList;
        this.ClientID = ClientID;
    }

    public void run()
    {
        while(true)
        {
                try {
                    Request request = (Request)clientList.get(ClientID).getIn().readObject();
                    if(request.equals(Request.MESSAGE))
                    {
                        String message = (String)(clientList.get(ClientID).getIn().readObject());
                        System.out.println("Message d'un client : " + message);

                        //on envoi le message a tous les clients
                        for(int i = 0; i<clientList.size(); i++)
                        {
                            //message non transmis à l'éméteur
                            if(i!=ClientID &&(clientList.get(ClientID).getTopicID() == clientList.get(i).getTopicID()))
                            {
                                clientList.get(i).getOut().writeObject(Request.MESSAGE);
                                clientList.get(i).getOut().writeObject(message);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

        }
    }
}

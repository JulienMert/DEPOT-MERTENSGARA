package client;

import server.Account;
import shared.Request;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Reception implements Runnable{

    private ObjectInputStream in;

    public Reception(ObjectInputStream in)
    {
        this.in = in;
    }
    public void run()
    {
        while (true)
        {

            try {
                //Account account = new Account(login, password);
                Request request = (Request) in.readObject();

                if (request.equals(Request.MESSAGE))
                {
                    System.out.println("Message d'un membre du chat :");
                    System.out.println(in.readObject());
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

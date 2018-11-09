package client;

import shared.Request;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Emission implements Runnable{

    private ObjectOutputStream out;
    private Scanner sc;

    public Emission(ObjectOutputStream out)
    {
        this.out = out;
    }

    public void run()
    {
        sc = new Scanner(System.in);
        while(true)
        {
            try {
                System.out.println("Votre message :");
                out.writeObject(Request.MESSAGE);
                out.writeObject(sc.next());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

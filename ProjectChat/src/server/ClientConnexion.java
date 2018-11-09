package server;

import java.io.*;
import java.net.*;

public class ClientConnexion {
    private Socket s;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int topicID = 0;

  public ClientConnexion(Socket s) throws IOException {
      this.s = s;
      this.out = new ObjectOutputStream(s.getOutputStream());
      this.in = new ObjectInputStream(s.getInputStream());
  }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public Socket getS() {
        return s;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }
}


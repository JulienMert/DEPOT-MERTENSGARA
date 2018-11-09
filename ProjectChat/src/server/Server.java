package server;

import java.io.*;
import java.util.*;

public class Server{

    private static Thread t1;
    private static Database db;
    private static ArrayList<Account> accountList;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        db = new Database("account.db");
        accountList = db.loadAccount();
        t1 = new Thread(new ClientHandler(accountList, db));
        t1.start();
        }
    }
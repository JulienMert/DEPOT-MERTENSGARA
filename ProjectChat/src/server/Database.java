package server;

import java.io.*;
import java.util.ArrayList;

public class Database {
    private File file;

    public Database(String fileName) {
        this.file = new File(fileName);
    }

    public ArrayList<Account> loadAccount() throws IOException, ClassNotFoundException {
        ArrayList<Account> data = new ArrayList<Account>();

        // On verifie si le fichier existe
        if(this.file.exists() && !this.file.isDirectory()) {

            FileInputStream fis = new FileInputStream(this.file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (ArrayList<Account>) ois.readObject();
            ois.close();
        } else {
            System.out.println("Le fichier de sauvegarde n'existe pas.");
        }

        System.out.println(data.size() + " Comptes(s) chargé(s) depuis la sauvegarde.");
        return data;
    }

    public void saveAccount(ArrayList<Account> data) throws IOException {

        FileOutputStream fos = new FileOutputStream(this.file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(data);
        oos.close();
        System.out.println("Sauvegarde effectuée... " + data.size() + " Compte(s) dans la base de données.");
    }
}
package server;

import java.io.Serializable;

public class Account implements Serializable {

    private String login;
    private String password;

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}

package com.acme.dbo.it.client;

public class Client {
    private int id;
    private String login;
    private String salt;
    private String secret;
    private String created;
    private boolean enabled;

    public Client() {
    }

    public Client(String login, String salt, String secret, String created, Boolean enabled) {
        this.login = login;
        this.salt = salt;
        this.secret = secret;
        this.created = created;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", salt='" + salt + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}

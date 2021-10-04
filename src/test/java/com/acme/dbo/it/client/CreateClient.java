package com.acme.dbo.it.client;

import javax.persistence.*;

@Entity(name = "createClient")
@Table(name = "CLIENT")
public class CreateClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String salt;
    private String secret;


    public CreateClient() {
    }

    public CreateClient(String login, String salt, String secret) {
        this.login = login;
        this.salt = salt;
        this.secret = secret;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return "CreateClient{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", salt='" + salt + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }

}

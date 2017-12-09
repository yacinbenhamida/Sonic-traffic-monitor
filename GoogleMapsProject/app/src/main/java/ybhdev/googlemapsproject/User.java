package ybhdev.googlemapsproject;

/**
 * Created by USER on 12/9/2017.
 */

public class User {
    private String username;
    private String password;
    private String mail;
    private String phNumber;
    private int idRegion;

    public User(String username, String password, String mail, String phNumber, int idRegion) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.phNumber = phNumber;
        this.idRegion = idRegion;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }
}

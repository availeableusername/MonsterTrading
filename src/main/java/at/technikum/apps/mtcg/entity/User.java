package at.technikum.apps.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonSetter;

public class User { //extends Entity

    //alles außer Konstruktor löschen?
    private String id;
    @JsonSetter("Username")
    private String Username;
    @JsonSetter("Password")
    private String Password;

    private int gold;

    private boolean exists = false;

    public User(){
        this.gold = 4;
    }

    public User(String Username, String Password) { //String description, boolean done
        this.Password = Password;
        this.Username = Username;
        //this.description = description;
        //this.done = done;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String name) {
        this.Username = name;
    }
    public String getPassword(){return this.Password;}
    public void setPassword(String password){this.Password = password;}

    public void setExists(){
        exists = true;
    }
    public boolean getExists(){
        return exists;
    }
}


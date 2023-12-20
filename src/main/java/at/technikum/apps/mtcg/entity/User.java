package at.technikum.apps.mtcg.entity;

public class User extends Entity {

    //alles außer Konstruktor löschen?
    private String id;
    private String name;
    private String password;

    public User(){

    }

    public User(String name, String password) { //String description, boolean done
        this.password = password;
        this.name = name;
        //this.description = description;
        //this.done = done;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPassword(){return this.password;}
}

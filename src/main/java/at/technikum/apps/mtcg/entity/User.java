package at.technikum.apps.mtcg.entity;

public class User extends Entity {
    private String id;
    private String name;
    private String description;
    private boolean done;

    public User(){

    }

    public User(String id, String name) { //String description, boolean done
        this.id = id;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return this.done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

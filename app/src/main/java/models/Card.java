package models;

public class Card {

    String id,name,email,mob,dlno,dlurl;
    boolean assign;
    int status;


    public Card() {
    }

    public Card(String id, String name, String email, String mob, String dlno, String dlurl, int status, boolean assign) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mob = mob;
        this.dlno = dlno;
        this.dlurl = dlurl;
        this.status = status;
        this.assign = assign;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getDlno() {
        return dlno;
    }

    public void setDlno(String dlno) {
        this.dlno = dlno;
    }

    public String getDlurl() {
        return dlurl;
    }

    public void setDlurl(String dlurl) {
        this.dlurl = dlurl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isAssign() {
        return assign;
    }

    public void setAssign(boolean assign) {
        this.assign = assign;
    }
}

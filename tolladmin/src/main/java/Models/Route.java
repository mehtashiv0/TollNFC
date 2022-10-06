package Models;

public class Route {

    String id, from, to;
    Double sp, rp;
    String Highway;

    public Route() {
    }

    public Route(String id, String from, String to, Double sp, Double rp, String highway) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.sp = sp;
        this.rp = rp;
        Highway = highway;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getSp() {
        return sp;
    }

    public void setSp(Double sp) {
        this.sp = sp;
    }

    public Double getRp() {
        return rp;
    }

    public void setRp(Double rp) {
        this.rp = rp;
    }

    public String getHighway() {
        return Highway;
    }

    public void setHighway(String highway) {
        Highway = highway;
    }
}

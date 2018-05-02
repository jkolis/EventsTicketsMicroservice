package eventstickets.endpoint;

public class Response {

    public boolean status;

    public static Response trueStatus() {
        Response res = new Response();
        res.setStatus(true);
        return res;
    }

    public static Response falseStatus() {
        Response res = new Response();
        res.setStatus(false);
        return res;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

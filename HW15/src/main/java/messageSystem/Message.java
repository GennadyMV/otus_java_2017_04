package messageSystem;

/**
 * @author sergey
 * created on 22.07.17.
 */
public abstract class Message {
    private Address from;
    private Address to;

    protected void setFrom(Address from) {
        this.from = from;
    }

    protected void setTo(Address to) {
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract MessageResponse exec();
}

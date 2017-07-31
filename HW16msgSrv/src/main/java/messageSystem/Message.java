package messageSystem;

/**
 * @author sergey
 * created on 22.07.17.
 */
public abstract class Message {
    public static final String CLASS_NAME_VARIABLE = "className";
    private final String className;

    private Address from;
    private Address to;

    public void setFrom(Address from) {
        this.from = from;
    }

    public void setTo(Address to) {
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    public Address getTo() {
        return to;
    }

    public abstract MessageResponse exec();

    protected Message(Class<?> klass) {
        this.className = klass.getName();
    }

    @Override
    public String toString() {
        return "Message{" +
                "className='" + className + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}

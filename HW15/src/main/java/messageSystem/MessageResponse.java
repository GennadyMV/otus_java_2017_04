package messageSystem;

/**
 * @author sergey
 * created on 23.07.17.
 */
public class MessageResponse<T> {
    private T value;

    public MessageResponse(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "value=" + value +
                '}';
    }
}

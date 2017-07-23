package messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sergey
 * created on 22.07.17.
 */
public class Address {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final String id;

    public Address(String name){
        id = name + ":" + String.valueOf(ID_GENERATOR.getAndIncrement());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        return id != null ? id.equals(address.id) : address.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                '}';
    }
}

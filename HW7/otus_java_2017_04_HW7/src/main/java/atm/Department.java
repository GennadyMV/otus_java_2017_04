package atm;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sergey
 *         created on 21.05.17.
 */
public class Department {
    private final List<Atm> atmList;
    private final Map<Atm, AtmMemento> savedStates;

    public Department(List<Atm> atmList) {
        this.atmList = Collections.unmodifiableList(atmList);
        savedStates = Collections.unmodifiableMap(this.atmList.stream()
                .collect(Collectors.toMap(atm -> atm, atm -> atm.saveToMemento())));
    }

    public long getBalance() {
        return atmList.stream().mapToLong(Atm::getBalance).sum();
    }

    public void restoreAtms() {
        savedStates.forEach((atm, savedState) -> atm.restoreFromMemento(savedState));
    }
}

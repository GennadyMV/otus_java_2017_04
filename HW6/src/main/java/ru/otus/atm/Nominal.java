package ru.otus.atm;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public enum Nominal {
    NOMINAL_10(10), NOMINAL_50(50), NOMINAL_100(100), NOMINAL_500(500), NOMINAL_1000(1000), NOMINAL_5000(5000);

    private int value;

    Nominal(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    static public Nominal getNominal(int value) {
        for (Nominal nom: Nominal.values()) {
            if (nom.getValue() == value) {
                return nom;
            }
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}

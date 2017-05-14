package ru.otus.atm;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public class Banknote {
    private final Nominal nominal;

    public Banknote(Nominal nominal) {
        this.nominal = nominal;
    }

    public Nominal getNominal() {
        return nominal;
    }
}

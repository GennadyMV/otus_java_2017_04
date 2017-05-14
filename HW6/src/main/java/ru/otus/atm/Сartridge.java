package ru.otus.atm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public class Сartridge {
    private final Nominal nominal;
    private final int banknoteLimit;
    private Queue<Banknote> banknotes = new LinkedList<>();

    public Сartridge(Nominal nominal, int banknoteLimit) {
        this.nominal = nominal;
        this.banknoteLimit = banknoteLimit;
    }

    public void encashment(final Banknote banknote) {
        banknotes.add(banknote);
    }

    public List<Banknote> withdrawal(int count) {
        List<Banknote> result = new ArrayList<>();
        for (int idx = 0; idx < count && banknotes.size() > 0; idx++) {
            result.add(banknotes.poll());
        }
        return result;
    }

    public Nominal getNominal() {
        return nominal;
    }

    public int getBanknoteLimit() {
        return banknoteLimit;
    }

    public int getRestCount() {
        return banknotes.size();
    }

    public int getRestSumm() {
        return banknotes.size() * nominal.getValue();
    }


}

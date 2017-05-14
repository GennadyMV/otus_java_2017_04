package ru.otus.atm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author sergey
 *         created on 14.05.17.
 */
public class CartridgeSetNominal {
    private final Nominal nominal;
    private final List<Сartridge> cartridges;

    public CartridgeSetNominal(Nominal nominal, List<Сartridge> cartridges) {
        this.nominal = nominal;
        this.cartridges = cartridges;
    }

    public void encashment(Banknote note) throws AtmException {
        Сartridge cartToAdd = cartridges.stream().
        filter(cart -> cart.getRestCount() < cart.getBanknoteLimit()).findFirst().orElse(null);
        if (cartToAdd == null) {
            throw new AtmException("Empty cartridge not found, nominal:" + note.getNominal());
        }
        cartToAdd.encashment(note);

    }

    public List<Banknote> withdrawal(int summa) throws AtmException {
        if (summa > getRest()) {
            throw new AtmException("Summa is too large, summa:" + summa + ", rest:" + getRest());
        }
        if (summa % nominal.getValue() != 0) {
            throw new AtmException("Summa is not applicable, summa:" + summa + ", nominal:" + getNominal());
        }

        List<Banknote> result = new ArrayList<>();

        final int needCounter = summa / nominal.getValue();
        int restCounter = needCounter;
        while(restCounter > 0) {
            Сartridge cartr = cartridges.stream().
                    filter(cart -> cart.getRestSumm() > 0).findFirst().orElse(null);
            result.addAll(cartr.withdrawal(Math.min(restCounter, cartr.getRestCount())));
            restCounter = needCounter - result.size();
        }

        return result;
    }

    public long getRest() {
        return cartridges.stream().mapToInt(Сartridge::getRestSumm).sum();

    }

    public Nominal getNominal() {
        return nominal;
    }
}

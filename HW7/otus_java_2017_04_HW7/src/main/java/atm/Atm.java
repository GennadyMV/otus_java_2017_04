package atm;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public class Atm {
    private Cartridge firstCartridge;

    public Atm(List<Cartridge> cartridgeList) {
        Collections.sort(cartridgeList);
        firstCartridge = cartridgeList.get(0);
        linkCartridges(cartridgeList);
    }


    private void linkCartridges(List<Cartridge> cartridgeList) {
        Iterator<Cartridge> iterator = cartridgeList.iterator();
        Cartridge cartridgeCurrent = iterator.next();
        while (iterator.hasNext()) {
            Cartridge cartridgeNext = iterator.next();
            cartridgeCurrent.setNextCartridge(cartridgeNext);
            cartridgeCurrent = cartridgeNext;
        }
    }

    public boolean withdraw(long requested) {
        return firstCartridge.withdraw(requested);
    }

    public long getBalance() {
        Iterator<Cartridge> iterator = firstCartridge.iterator();
        long balance = 0;
        while (iterator.hasNext()) {
            balance += iterator.next().getBalance();
        }
        return balance;
    }

    public AtmMemento saveToMemento() {
        return new AtmMemento(firstCartridge);
    }

    public void restoreFromMemento(AtmMemento memento) {
        this.firstCartridge = memento.getSavedState();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Atm atm = (Atm) o;

        return firstCartridge != null ? firstCartridge.equals(atm.firstCartridge) : atm.firstCartridge == null;
    }

    @Override
    public int hashCode() {
        return firstCartridge != null ? firstCartridge.hashCode() : 0;
    }
}

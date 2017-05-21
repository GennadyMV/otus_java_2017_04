package atm;

import java.util.Iterator;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public class Cartridge implements Comparable<Cartridge>, Iterable<Cartridge> {
    private final Nominal nominal;
    private int count;
    private Cartridge nextCartridge;

    public Cartridge(Nominal nominal, int count) {
        this.nominal = nominal;
        this.count = count;
    }

    public Cartridge(Cartridge srcCartridge) {
        this.nominal = srcCartridge.getNominal();
        this.count = srcCartridge.getCount();
        this.nextCartridge = srcCartridge.nextCartridge;
    }

    public boolean withdraw(long requested) {
        long expectedCount = Math.min(requested / nominal.getValue(), count);
        long expectedCash = expectedCount * nominal.getValue();
        boolean nextСartridgeResult = true;
        if (requested != expectedCash) {
            nextСartridgeResult = nextCartridge != null && nextCartridge.withdraw(requested - expectedCash);
        }
        if(nextСartridgeResult) {
            count -= expectedCount;
            return true;
        }
        return false;
    }

    public Nominal getNominal() {
        return nominal;
    }

    public int getCount() {
        return count;
    }

    public int getBalance() {
        return count * nominal.getValue();
    }

    public void setNextCartridge(Cartridge nextCartridge) {
        this.nextCartridge = nextCartridge;
    }

    @Override
    public int compareTo(Cartridge o) {
        if (this == o) {
            return 0;
        }

        if (nominal.getValue() > o.getNominal().getValue())
            return -1;
        if (nominal.getValue() < o.getNominal().getValue())
            return 1;
        return 0;
    }

    @Override
    public Iterator<Cartridge> iterator() {
        return new Iterator<Cartridge>() {
            Cartridge current = Cartridge.this;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Cartridge next() {
                Cartridge before = current;
                current = current.nextCartridge;
                return before;
            }
        };
    }


}




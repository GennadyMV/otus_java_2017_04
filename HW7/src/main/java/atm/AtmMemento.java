package atm;

import java.util.Iterator;

/**
 * @author sergey
 *         created on 21.05.17.
 */
public class AtmMemento {
    private final Cartridge  state;

    public AtmMemento(Cartridge  stateToSave) {
        state = new Cartridge(stateToSave);

        Iterator<Cartridge> iterator = state.iterator();
        Cartridge cartridgeCurrent = iterator.next();
        while (iterator.hasNext()) {
            Cartridge cartridgeNext = new Cartridge(iterator.next());
            cartridgeCurrent.setNextCartridge(cartridgeNext);
            cartridgeCurrent = cartridgeNext;
        }
    }

    public Cartridge getSavedState() {
        return state;
    }
}

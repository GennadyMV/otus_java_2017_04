package atm;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author sergey
 *         created on 21.05.17.
 */
public class CartridgeTest {


    @Test
    public void withdraw_part() throws Exception {
        final Nominal nominal = Nominal.NOMINAL_50;
        final int count = 3;
        Cartridge cartridge = new Cartridge(Nominal.NOMINAL_50, count);
        assertTrue("withdraw part. ok", cartridge.withdraw(nominal.getValue() * (count - 1)));
    }

    @Test
    public void withdraw_full() throws Exception {
        final Nominal nominal = Nominal.NOMINAL_50;
        final int count = 2;
        Cartridge cartridge = new Cartridge(nominal, count);
        assertTrue("withdraw full. ok", cartridge.withdraw(nominal.getValue() * count));
    }

    @Test
    public void withdraw_notPart() throws Exception {
        final Nominal nominal = Nominal.NOMINAL_50;
        final int count = 2;
        Cartridge cartridge = new Cartridge(nominal, count);
        assertFalse("withdraw NotPart. fail", cartridge.withdraw(nominal.getValue() - 1));
    }

    @Test
    public void withdraw_overSumm() throws Exception {
        final Nominal nominal = Nominal.NOMINAL_50;
        final int count = 2;
        Cartridge cartridge = new Cartridge(nominal, count);
        assertFalse("withdraw OverSumm. fail", cartridge.withdraw(nominal.getValue() * count + 10));
    }


    @Test
    public void getNominal() throws Exception {
        final Nominal nominal =  Nominal.NOMINAL_50;
        Cartridge cartridge = new Cartridge(nominal, 1);
        assertEquals("nominal ok", nominal , cartridge.getNominal());
    }

    @Test
    public void getCount() throws Exception {
        final int count = 2;
        final Nominal nominal =  Nominal.NOMINAL_50;
        Cartridge cartridge = new Cartridge(nominal, count);

        assertEquals("count ok", count, cartridge.getCount());
        cartridge.withdraw(nominal.getValue());
        assertEquals("count after withdraw ok", count -1 , cartridge.getCount());

    }

    @Test
    public void getBalance() throws Exception {
        final Nominal nominal = Nominal.NOMINAL_50;
        final int count = 2;
        Cartridge cartridge = new Cartridge(nominal, count);
        assertEquals("balance. ok", nominal.getValue() * count, cartridge.getBalance());
    }


    @Test
    public void compareTo() throws Exception {
        final Nominal nominalSmall = Nominal.NOMINAL_50;
        final Nominal nominalBig = Nominal.NOMINAL_100;

        final Cartridge cartridgeSmall = new Cartridge(nominalSmall, 1);
        final Cartridge cartridgeBig = new Cartridge(nominalBig, 1);
        final Cartridge cartridgeBig2 = new Cartridge(nominalBig, 1);

        assertEquals("Small - big. ok", 1, cartridgeSmall.compareTo(cartridgeBig));
        assertEquals("big - Small. ok", -1, cartridgeBig.compareTo(cartridgeSmall));
        assertEquals("self. ok", 0, cartridgeBig.compareTo(cartridgeBig));
        assertEquals("big - big. ok", 0, cartridgeBig.compareTo(cartridgeBig2));
    }

    @Test
    public void iterator() throws Exception {
        final Cartridge cartridge1 = new Cartridge(Nominal.NOMINAL_50, 1);
        final Cartridge cartridge2 = new Cartridge(Nominal.NOMINAL_50, 1);
        final Cartridge cartridge3 = new Cartridge(Nominal.NOMINAL_50, 1);
        cartridge1.setNextCartridge(cartridge2);
        cartridge2.setNextCartridge(cartridge3);

        Iterator iter = cartridge1.iterator();
        assertTrue("has next", iter.hasNext());
        assertEquals("cartridge1", cartridge1, iter.next());
        assertEquals("cartridge2", cartridge2, iter.next());
        assertEquals("cartridge3", cartridge3, iter.next());
        assertFalse("no next", iter.hasNext());

    }

}
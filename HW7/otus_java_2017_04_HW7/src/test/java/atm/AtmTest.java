package atm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sergey
 *         created on 21.05.17.
 */
public class AtmTest {

    @Test
    public void withdraw_oneCartridge() throws Exception {
        final Nominal nominal1 = Nominal.NOMINAL_50;
        final int count1 = 2;
        final Nominal nominal2 = Nominal.NOMINAL_100;
        final int count2 = 3;

        final List<Cartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(new Cartridge(nominal1, count1));
        cartridgeList.add(new Cartridge(nominal2, count2));

        Atm atm = new Atm(cartridgeList);

        assertTrue("withdraw one cartridge", atm.withdraw(nominal1.getValue()));
    }

    @Test
    public void withdraw_severalFull() throws Exception {
        final Nominal nominal1 = Nominal.NOMINAL_50;
        final int count1 = 2;
        final Nominal nominal2 = Nominal.NOMINAL_100;
        final int count2 = 3;

        final List<Cartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(new Cartridge(nominal1, count1));
        cartridgeList.add(new Cartridge(nominal2, count2));

        Atm atm = new Atm(cartridgeList);
        assertTrue("withdraw several (nominal) cartridge", atm.withdraw(nominal2.getValue()));
    }

    @Test
    public void withdraw_severalNominal() throws Exception {
        final Nominal nominal1 = Nominal.NOMINAL_50;
        final int count1 = 2;
        final Nominal nominal2 = Nominal.NOMINAL_100;
        final int count2 = 3;

        final List<Cartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(new Cartridge(nominal1, count1));
        cartridgeList.add(new Cartridge(nominal2, count2));

        Atm atm = new Atm(cartridgeList);
        assertTrue("withdraw several (nominal) cartridge", atm.withdraw(nominal2.getValue()));
    }

    @Test
    public void getBalance() throws Exception {
        final Nominal nominal1 = Nominal.NOMINAL_50;
        final int count1 = 2;
        final Nominal nominal2 = Nominal.NOMINAL_100;
        final int count2 = 3;

        final List<Cartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(new Cartridge(nominal1, count1));
        cartridgeList.add(new Cartridge(nominal2, count2));

        Atm atm = new Atm(cartridgeList);

        assertEquals("balance", nominal1.getValue() * count1 + nominal2.getValue() * count2, atm.getBalance());

    }

}
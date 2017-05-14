package ru.otus.atm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public class СartridgeTest {

    @Test
    public void construct() throws Exception {
        final int limit = 100;
        final Nominal nominal = Nominal.NOMINAL_50;
        Сartridge cartridge = new Сartridge(nominal, limit);

        assertEquals("check Nominal", nominal, cartridge.getNominal());
        assertEquals("check Limit", limit, cartridge.getBanknoteLimit());
    }


    @Test
    public void encashment() throws Exception {
        Nominal nominal = Nominal.NOMINAL_50;
        Сartridge cartridge = new Сartridge(nominal, 100);


        cartridge.encashment(new Banknote(nominal));
        cartridge.encashment(new Banknote(nominal));
        cartridge.encashment(new Banknote(nominal));

        assertEquals("check Rest count", 3, cartridge.getRestCount());
        assertEquals("check Rest summ", 3 * nominal.getValue(), cartridge.getRestSumm());
    }

    @Test
    public void withdrawal() throws Exception {
        Сartridge cartridge = new Сartridge(Nominal.NOMINAL_50, 100);

        cartridge.encashment(new Banknote(Nominal.NOMINAL_50));
        cartridge.encashment(new Banknote(Nominal.NOMINAL_50));
        cartridge.encashment(new Banknote(Nominal.NOMINAL_50));

        final int withdrawalCount = 2;
        List<Banknote> cash = cartridge.withdrawal(withdrawalCount);

        assertEquals("check Cash", withdrawalCount, cash.size());
        assertEquals("check Rest", 3 - withdrawalCount, cartridge.getRestCount());
    }

}
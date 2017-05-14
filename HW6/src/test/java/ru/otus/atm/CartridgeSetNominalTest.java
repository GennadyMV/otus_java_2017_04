package ru.otus.atm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sergey
 *         created on 14.05.17.
 */
public class CartridgeSetNominalTest {

    private final int limit = 3;
    private final Nominal nominal = Nominal.NOMINAL_50;

    @Test
    public void encashment() throws Exception {
        Сartridge cartridge1 = new Сartridge(nominal, limit);
        Сartridge cartridge2 = new Сartridge(nominal, limit);
        List<Сartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(cartridge1);
        cartridgeList.add(cartridge2);

        CartridgeSetNominal cartridgeSetNominal= new CartridgeSetNominal(nominal, cartridgeList);


        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));

        assertEquals("encashment summ", 6 * nominal.getValue(), cartridgeSetNominal.getRest());
    }

    @Test
    public void withdrawal() throws Exception {
        Сartridge cartridge1 = new Сartridge(nominal, limit);
        Сartridge cartridge2 = new Сartridge(nominal, limit);
        List<Сartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(cartridge1);
        cartridgeList.add(cartridge2);
        CartridgeSetNominal cartridgeSetNominal= new CartridgeSetNominal(nominal, cartridgeList);


        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));
        cartridgeSetNominal.encashment(new Banknote(nominal));

        final int withdrawalSumm = 100;
        final int summRest = 6 * nominal.getValue() - withdrawalSumm ;
        cartridgeSetNominal.withdrawal(withdrawalSumm);

        assertEquals("withdrawal check", summRest, cartridgeSetNominal.getRest());
    }

}
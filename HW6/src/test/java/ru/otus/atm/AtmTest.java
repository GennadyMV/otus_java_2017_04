package ru.otus.atm;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public class AtmTest {

    private final int limit = 100;
    private final Nominal nominal1 = Nominal.NOMINAL_50;
    private final Nominal nominal2 = Nominal.NOMINAL_500;
    private final Nominal nominal3 = Nominal.NOMINAL_5000;

    @Test
    public void encashment() throws Exception {
        Сartridge cartridge1 = new Сartridge(nominal1, limit);
        Сartridge cartridge2 = new Сartridge(nominal2, limit);
        List<Сartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(cartridge1);
        cartridgeList.add(cartridge2);
        Atm atm = new Atm(cartridgeList);

        List<Banknote> banknoteList = new ArrayList<>();
        banknoteList.add(new Banknote(nominal1));
        banknoteList.add(new Banknote(nominal1));
        banknoteList.add(new Banknote(nominal1));
        banknoteList.add(new Banknote(nominal1));
        banknoteList.add(new Banknote(nominal2));
        banknoteList.add(new Banknote(nominal2));

        atm.encashment(banknoteList);
        final int summ = banknoteList.stream().mapToInt(note -> note.getNominal().getValue()).sum();
        assertEquals("encashment summ", summ, atm.getRest());
    }

    @Test(expected = AtmException.class)
    public void encashment_notFound() throws Exception {
        Сartridge cartridge1 = new Сartridge(nominal1, limit);
        Сartridge cartridge2 = new Сartridge(nominal2, limit);
        List<Сartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(cartridge1);
        cartridgeList.add(cartridge2);
        Atm atm = new Atm(cartridgeList);

        List<Banknote> banknoteList = new ArrayList<>();
        banknoteList.add(new Banknote(nominal3));
        atm.encashment(banknoteList);

    }

    @Test(expected = AtmException.class)
    public void encashment_notFoundEmpty() throws Exception {
        Сartridge cartridge1 = new Сartridge(nominal1, limit);
        Сartridge cartridge2 = new Сartridge(nominal2, 1);
        List<Сartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(cartridge1);
        cartridgeList.add(cartridge2);
        Atm atm = new Atm(cartridgeList);

        List<Banknote> banknoteList = new ArrayList<>();
        banknoteList.add(new Banknote(nominal1));
        banknoteList.add(new Banknote(nominal1));
        banknoteList.add(new Banknote(nominal1));
        banknoteList.add(new Banknote(nominal2));
        banknoteList.add(new Banknote(nominal2));
        banknoteList.add(new Banknote(nominal2));

        atm.encashment(banknoteList);
    }

    @Test
    public void withdrawal() throws Exception {
        Сartridge cartridge1 = new Сartridge(nominal1, limit);
        Сartridge cartridge2 = new Сartridge(nominal2, limit);
        List<Сartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(cartridge1);
        cartridgeList.add(cartridge2);
        Atm atm = new Atm(cartridgeList);

        List<Banknote> banknoteList = new ArrayList<>();
        banknoteList.add(new Banknote(nominal1)); //50
        banknoteList.add(new Banknote(nominal1)); //50
        banknoteList.add(new Banknote(nominal1)); //50
        banknoteList.add(new Banknote(nominal1)); //50
        banknoteList.add(new Banknote(nominal2)); //500
        banknoteList.add(new Banknote(nominal2)); //500

        atm.encashment(banknoteList);
        final int withdrawalSumm = 700;
        final int summRest = banknoteList.stream().mapToInt(note -> note.getNominal().getValue()).sum() - withdrawalSumm ;
        List<Banknote> result = atm.withdrawal(withdrawalSumm);

        assertEquals("withdrawal check summ", withdrawalSumm, result.stream().mapToInt(note -> note.getNominal().getValue()).sum());
        assertEquals("withdrawal check count", 5, result.size());
        assertEquals("withdrawal check rest", summRest, atm.getRest());
    }


}
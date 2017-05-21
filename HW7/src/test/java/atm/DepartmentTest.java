package atm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author sergey
 *         created on 21.05.17.
 */
public class DepartmentTest {

    private Atm createTestAtm(Nominal nominal1, int count1, Nominal nominal2, int count2) {
        final List<Cartridge> cartridgeList = new ArrayList<>();
        cartridgeList.add(new Cartridge(nominal1, count1));
        cartridgeList.add(new Cartridge(nominal2, count2));
        return new Atm(cartridgeList);
    }


    @Test
    public void getBalance() throws Exception {
        final Nominal nominal1 = Nominal.NOMINAL_50;
        final int count1 = 2;
        final Nominal nominal2 = Nominal.NOMINAL_100;
        final int count2 = 3;
        final Atm atm1 = createTestAtm(nominal1, count1, nominal2, count2);

        final Nominal nominal3 = Nominal.NOMINAL_500;
        final int count3 = 2;
        final Nominal nominal4 = Nominal.NOMINAL_1000;
        final int count4 = 4;
        Atm atm2 = createTestAtm(nominal3, count3, nominal4, count4);

        List<Atm> atmList = new ArrayList<>();
        atmList.add(atm1);
        atmList.add(atm2);

        Department department = new Department(atmList);

        assertEquals("balance", nominal1.getValue() * count1 + nominal2.getValue() * count2 +
                nominal3.getValue() * count3 + nominal4.getValue() * count4, department.getBalance());
    }

    @Test
    public void restoreAtms() throws Exception {
        final Atm atm1 = createTestAtm(Nominal.NOMINAL_50, 2, Nominal.NOMINAL_100, 3);
        final Atm atm2 = createTestAtm(Nominal.NOMINAL_500, 2, Nominal.NOMINAL_1000, 4);

        final long balanceAtm1Befor = atm1.getBalance();
        final long balanceAtm2Befor = atm2.getBalance();

        List<Atm> atmList = new ArrayList<>();
        atmList.add(atm1);
        atmList.add(atm2);

        Department department = new Department(atmList);
        atm1.withdraw(atm1.getBalance());
        atm2.withdraw(atm2.getBalance());

        department.restoreAtms();
        assertEquals("Atm1 restored", balanceAtm1Befor, atm1.getBalance());
        assertEquals("Atm2 restored", balanceAtm2Befor, atm2.getBalance());

    }

}
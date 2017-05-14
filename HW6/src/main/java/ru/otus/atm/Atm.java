package ru.otus.atm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sergey
 *         created on 13.05.17.
 */
public class Atm {
    private final Map<Nominal, CartridgeSetNominal> cartridgesSet = new HashMap<>();

    public Atm(final List<Сartridge> cartridgeList) {
        Map<Nominal, List<Сartridge>> cartridges = new HashMap<>();
        cartridgeList.forEach(cart -> {
            if (!cartridges.containsKey(cart.getNominal())) {
                cartridges.put(cart.getNominal(), new ArrayList<>());
            }
            cartridges.get(cart.getNominal()).add(cart);
        });
        for(Map.Entry<Nominal, List<Сartridge>> cartItem: cartridges.entrySet()) {
            cartridgesSet.put(cartItem.getKey(), new CartridgeSetNominal(cartItem.getKey(), cartItem.getValue()));
        }
    }

    public void encashment(final List<Banknote> banknoteList) throws AtmException {
        for (Banknote note: banknoteList) {
            CartridgeSetNominal cartridgeSetNominal = cartridgesSet.get(note.getNominal());
            if (cartridgeSetNominal == null) {
                throw new AtmException("Cartridge not found, nominal:" + note.getNominal());
            }
            cartridgeSetNominal.encashment(note);
        }
    }

    private List<Banknote> getBanknote(int partSumma) throws AtmException {
        Map<Nominal, CartridgeSetNominal> cartridgesHasRest = new HashMap<>();
        for(Map.Entry<Nominal, CartridgeSetNominal> cartItem: cartridgesSet.entrySet()) {
            if (cartItem.getValue().getRest() > 0) {
                cartridgesHasRest.put(cartItem.getKey(), cartItem.getValue());
            }
        }
        List<Integer> orderNominal = new ArrayList<>(cartridgesHasRest.keySet()).stream().map(Nominal::getValue)
                .sorted().collect(Collectors.toList());
        for (int idx  = orderNominal.size() - 1; idx >= 0; idx--) {
            int nominal = orderNominal.get(idx);
            if (partSumma >= nominal) {
               return cartridgesSet.get(Nominal.getNominal(nominal)).withdrawal(nominal);
            }
        }
        throw new AtmException("Requested summ cann't be withdrawaled");
    }

    private int calcBanknoteSumm(List<Banknote> batch) {
        return batch.stream().mapToInt(note -> note.getNominal().getValue()).sum();
    }

    public List<Banknote> withdrawal(int summa) throws AtmException {
        List<Banknote> result = new ArrayList<>();
        int partSumma = summa;
        while (partSumma > 0) {
            result.addAll(getBanknote(partSumma));
            partSumma = summa - calcBanknoteSumm(result);
        }
        return result;
    }

    public long getRest() {
        return cartridgesSet.values().stream().mapToLong(CartridgeSetNominal::getRest).sum();
    }
}

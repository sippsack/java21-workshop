package virtual.threads.client.scoped.values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CreditCardService {

    public static final CreditCardService CREDIT_SERVICE = new CreditCardService();

    private final Map<String, Amount> accounts;

    private CreditCardService() {
        accounts = new ConcurrentHashMap<>();
    }

    public Amount getBalance(CreditCard card) {
        return accounts.computeIfAbsent(card.number(), cc -> new Amount(0));
    }

    public Amount addBalance(CreditCard card, Amount delta) {
        return accounts.compute(card.number(), (k, v) -> {
            if (v == null) {
                return delta;
            } else {
                return v.plus(delta);
            }
        });
    }

}

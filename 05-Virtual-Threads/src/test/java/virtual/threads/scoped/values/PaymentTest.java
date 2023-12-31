package virtual.threads.scoped.values;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import virtual.threads.client.scoped.values.Amount;
import virtual.threads.client.scoped.values.CreditCard;
import virtual.threads.client.scoped.values.MakeIndirectPayment;

import static virtual.threads.client.scoped.values.CreditCardService.CREDIT_SERVICE;

public class PaymentTest {

    public static CreditCard FREDS_CARD = new CreditCard(
        "Fred Feuerstein",
        "123456789012",
        "12/28",
        "123"
    );

    public static CreditCard WILMAS_CARD = new CreditCard(
        "Wilma Feuerstein",
        "123456789013",
        "12/28",
        "123"
    );

    @Test
    public void testPayment() throws InterruptedException {

        var PRICE = new Amount(12.25);

        var payment = new MakeIndirectPayment();
        var t1 = Thread.ofVirtual().start(() -> payment.buySomething("Flintstein", PRICE, FREDS_CARD));
        var t2 = Thread.ofVirtual().start(() -> payment.buySomething("Flintstein", PRICE, WILMAS_CARD));
        t1.join();
        t2.join();

        var amount = CREDIT_SERVICE.getBalance(FREDS_CARD);
        Assertions.assertEquals(PRICE, amount);

    }

}

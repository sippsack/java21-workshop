
package virtual.threads.client.scoped.values;

import static virtual.threads.client.scoped.values.CreditCardService.CREDIT_SERVICE;

public class MakeIndirectPayment {

    private static final ScopedValue<CreditCard> CREDIT_CARD = ScopedValue.newInstance();

    public MakeIndirectPayment() {}

    public void buySomething(String product, Amount amount, CreditCard credit_card) {
        System.out.println(product + " für " + amount);
        ScopedValue.where(CREDIT_CARD, credit_card).run(() -> paySomething(amount));
    }

    /**
     * Diese Methode sorgt für den Zahlvorgang und darf/kann nicht verändert werden.
     *
     * @param amount Der zu zahlende Betrag
     */
    private void paySomething(Amount amount) {
        makePayment(amount);
    }

    private void makePayment(Amount amount) {
        // An dieser Stelle wird die Thread-lokale Kreditkarte benötigt.
        CreditCard card = CREDIT_CARD.get();

        CREDIT_SERVICE.addBalance(card, amount);
    }

}

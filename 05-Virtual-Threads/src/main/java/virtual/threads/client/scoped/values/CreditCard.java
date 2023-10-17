package virtual.threads.client.scoped.values;

import java.util.regex.Pattern;

public record CreditCard(String name, String number, String expiration_date, String security_code) {

    private final static Pattern VALID_PATTERN = Pattern.compile("\\d\\d/\\d\\d");

    public CreditCard {
        name = name.strip();
        number = number.replaceAll("\\D", "");
        checkValidCreditCardNumber(number);
        expiration_date = expiration_date.strip();
        checkValidExpirationDate(expiration_date);
        security_code = security_code.replaceAll("\\D", "");
        checkValidSecurityCode(security_code);
    }

    private static void checkValidCreditCardNumber(String number) {
        if (number.length() != 12) {
            throw new IllegalArgumentException(
                "die Kartennummer " + number + " hat nicht die erforderliche Anzahl Ziffern"
            );
        }
    }

    private static void checkValidExpirationDate(String expiration_date) {
        var matcher = VALID_PATTERN.matcher(expiration_date);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                "die Gültigkeitsdauer " + expiration_date + " hat nicht die erforderliche Form yy/mm"
            );
        }
    }

    private static void checkValidSecurityCode(String security_code) {
        if (security_code.length() != 3) {
            throw new IllegalArgumentException(
                "der Security Code " + security_code + " hat nicht die erforderliche Anzahl Ziffern"
            );
        }
    }

}

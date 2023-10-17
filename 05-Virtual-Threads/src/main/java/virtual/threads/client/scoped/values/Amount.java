package virtual.threads.client.scoped.values;

public record Amount(long amount) {

    private static final long SCALE = 100;

    public Amount(double amount) {
        this((long) (amount * 100.0));
    }

    public Amount plus(Amount other) {
        return new Amount(amount + other.amount);
    }

    public String toString() {
        return String.format("%d.%02d €", amount / SCALE, amount % SCALE);
    }

}

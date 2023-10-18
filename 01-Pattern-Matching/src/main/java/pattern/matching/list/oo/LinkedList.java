package pattern.matching.list.oo;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class LinkedList<T> implements List<T> {

    private final LinkedList<T> next;
    private final T value;

    private LinkedList(LinkedList<T> next, T value) {
        this.next = next;
        this.value = value;
    }

    public static void main(String[] args) {
        List<Integer> list = of(1, 2, 3);
        System.out.println(list);
        System.out.println(list.head());
        System.out.println(list.tail());
        System.out.println(list.contains(5));
        System.out.println(list.contains(1));

    }

    @SafeVarargs
    public static <T> List<T> of(T... values) {
        if (values.length == 0) {
            return new LinkedList<>(null, null);
        }

        LinkedList<T> current = null;
        for (int i = values.length; i >= 1; i--) {
            current = new LinkedList<>(current, values[i - 1]);
        }
        return current;
    }

    @Override
    public T head() {
        if (isNull(value)) throw new IndexOutOfBoundsException("List is empty");
        else return this.value;
    }

    @Override
    public List<T> tail() {
        return nonNull(next) ? this.next : LinkedList.of();
    }

    @Override
    public boolean contains(T value) {
        return this.value != null && (this.value.equals(value) || tail().contains(value));
    }

    @Override
    public String toString() {
        return "[%s] -> %s".formatted(value, next != null ? next.toString() : "NIL");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LinkedList<?> other
               && Objects.equals(this.value, other.value)
               && Objects.equals(this.next, other.next);
    }
}

package pattern.matching.list.oo;

public interface List<T> {
    /**
     * @return first element of the list
     * @throws IndexOutOfBoundsException if list is empty
     */
    T head();

    /**
     * @return list without the first element
     */
    List<T> tail();

    /**
     * @param value to determine
     * @return true if the value is in the list
     */
    boolean contains(T value);
}

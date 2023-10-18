package pattern.matching.list.oo;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LinkedListTest {

    @Test
    void testOOLinkedList() {
        List<Integer> list = LinkedList.of(1, 2, 3);
        assertThat(list).asString().contains("[1] -> [2] -> [3] -> NIL");
        assertThat(list.head()).isEqualTo(1);
        assertThat(list.tail()).asString().contains("[2] -> [3] -> NIL");
        assertThat(list.contains(3)).isTrue();
        assertThat(list.contains(5)).isFalse();
    }
}
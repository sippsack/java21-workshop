package sequenced.collections;

import java.util.*;

public class SequencedCollections {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        System.out.println(list);
        List<Integer> reversed = list.reversed();
        System.out.println(reversed);

        list.addFirst(0);
        list.addLast(6);

        System.out.println(list);
        System.out.println(reversed);

        System.out.println(list.getFirst());
        System.out.println(list.getLast());

        SequencedMap<Integer, String> map = new LinkedHashMap<>(Map.of(1, "a", 2, "b"));
        SequencedMap<Integer, String> reversedMap = map.reversed();
        System.out.println(map);
        System.out.println(reversedMap);

        map.putFirst(0, "o");
        map.putLast(3, "c");

        System.out.println(map);

        System.out.println(map.pollFirstEntry());
        System.out.println(map);
    }
}

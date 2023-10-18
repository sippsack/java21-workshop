package sequenced.collections;

import java.util.*;

public class SequencedCollections {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5));

        SequencedMap<Integer, String> map = new LinkedHashMap<>(Map.of(1, "a", 2, "b"));
    }
}

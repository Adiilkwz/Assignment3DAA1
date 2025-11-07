import java.util.*;

public class UnionFind {
    Map<String, String> parent = new HashMap<>();

    public void makeSet(Collection<String> vertices) {
        for (String v : vertices) parent.put(v, v);
    }

    public String find(String v) {
        if (!parent.get(v).equals(v))
            parent.put(v, find(parent.get(v))); // path compression
        return parent.get(v);
    }

    public void union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);
        if (!rootA.equals(rootB)) parent.put(rootA, rootB);
    }
}
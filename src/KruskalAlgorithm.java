import java.util.*;
import org.json.simple.*;

public class KruskalAlgorithm {

    static class Edge implements Comparable<Edge> {
        String from, to;
        double weight;

        Edge(String f, String t, double w) {
            from = f;
            to = t;
            weight = w;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }

    static class UnionFind {
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

    @SuppressWarnings("unchecked")
    public static JSONObject runKruskal(List<String> nodes, JSONArray edges) {
        List<Edge> edgeList = new ArrayList<>();
        for (Object obj : edges) {
            JSONObject e = (JSONObject) obj;
            String from = (String) e.get("from");
            String to = (String) e.get("to");
            double w = ((Number) e.get("weight")).doubleValue();
            edgeList.add(new Edge(from, to, w));
        }

        long startTime = System.nanoTime();
        int operations = 0;
        Collections.sort(edgeList);
        UnionFind uf = new UnionFind();
        uf.makeSet(nodes);

        List<Edge> mst = new ArrayList<>();
        double totalCost = 0;

        for (Edge e : edgeList) {
            operations++;
            String rootFrom = uf.find(e.from);
            String rootTo = uf.find(e.to);
            if (!rootFrom.equals(rootTo)) {
                uf.union(e.from, e.to);
                mst.add(e);
                totalCost += e.weight;
            }
        }

        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        JSONArray mstArray = new JSONArray();
        for (Edge e : mst) {
            JSONObject edgeObj = new JSONObject();
            edgeObj.put("from", e.from);
            edgeObj.put("to", e.to);
            edgeObj.put("weight", e.weight);
            mstArray.add(edgeObj);
        }

        JSONObject result = new JSONObject();
        result.put("mst_edges", mstArray);
        result.put("total_cost", totalCost);
        result.put("operations_count", operations);
        result.put("execution_time_ms", durationMs);

        return result;
    }
}

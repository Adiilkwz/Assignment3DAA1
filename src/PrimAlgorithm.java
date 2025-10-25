import java.util.*;
import org.json.simple.*;
import java.util.ArrayList;

public class PrimAlgorithm {

    static class Edge {
        String from, to;
        double weight;
        Edge(String f, String t, double w) { from = f; to = t; weight = w; }
    }

    static class Node implements Comparable<Node> {
        String vertex;
        double cost;
        String parent;
        Node(String v, double c, String p) { vertex = v; cost = c; parent = p; }
        public int compareTo(Node other) {
            return Double.compare(this.cost, other.cost);
        }
    }

    @SuppressWarnings("unchecked")
    public static JSONObject runPrim(List<String> nodes, JSONArray edges) {
        // Build adjacency list
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String node : nodes) {
            adj.put(node, new ArrayList<Edge>());
        }

        for (Object obj : edges) {
            JSONObject e = (JSONObject) obj;
            String from = (String) e.get("from");
            String to = (String) e.get("to");
            double w = ((Number) e.get("weight")).doubleValue();
            adj.get(from).add(new Edge(from, to, w));
            adj.get(to).add(new Edge(to, from, w)); // undirected
        }

        long startTime = System.nanoTime();
        int operations = 0;
        Set<String> visited = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        List<Edge> mstEdges = new ArrayList<>();
        double totalCost = 0;

        String startNode = nodes.get(0);
        pq.add(new Node(startNode, 0, null));

        while (!pq.isEmpty() && visited.size() < nodes.size()) {
            Node current = pq.poll();
            operations++;
            if (visited.contains(current.vertex)) continue;

            visited.add(current.vertex);
            if (current.parent != null) {
                mstEdges.add(new Edge(current.parent, current.vertex, current.cost));
                totalCost += current.cost;
            }

            for (Edge edge : adj.get(current.vertex)) {
                operations++;
                if (!visited.contains(edge.to)) {
                    pq.add(new Node(edge.to, edge.weight, current.vertex));
                }
            }
        }

        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        // Prepare JSON result
        JSONObject primResult = new JSONObject();
        JSONArray mstEdgeArray = new JSONArray();
        for (Edge e : mstEdges) {
            JSONObject edgeObj = new JSONObject();
            edgeObj.put("from", e.from);
            edgeObj.put("to", e.to);
            edgeObj.put("weight", e.weight);
            mstEdgeArray.add(edgeObj);
        }
        primResult.put("mst_edges", mstEdgeArray);
        primResult.put("total_cost", totalCost);
        primResult.put("operations_count", operations);
        primResult.put("execution_time_ms", durationMs);

        return primResult;
    }
}

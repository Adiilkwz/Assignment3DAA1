public class Node implements Comparable<Node> {
    String vertex;
    double cost;
    String parent;
    Node(String v, double c, String p) { vertex = v; cost = c; parent = p; }
    public int compareTo(Node other) {
        return Double.compare(this.cost, other.cost);
    }
}
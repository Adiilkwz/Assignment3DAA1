public class Edge implements Comparable<Edge> {
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
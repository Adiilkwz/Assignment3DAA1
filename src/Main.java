import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        List<Map<String, Object>> resultsList = new ArrayList<>();

        try (Reader reader = new FileReader("input_graphs.json")) {
            JSONObject inputData = (JSONObject) parser.parse(reader);
            JSONArray graphs = (JSONArray) inputData.get("graphs");

            for (Object obj : graphs) {
                JSONObject graph = (JSONObject) obj;

                int graphId = ((Long) graph.get("id")).intValue();
                JSONArray nodesArray = (JSONArray) graph.get("nodes");
                JSONArray edgesArray = (JSONArray) graph.get("edges");

                List<String> nodes = new ArrayList<>();
                for (Object n : nodesArray) nodes.add((String) n);

                // Input stats
                Map<String, Object> inputStats = new LinkedHashMap<>();
                inputStats.put("vertices", nodes.size());
                inputStats.put("edges", edgesArray.size());

                // Run Prim's Algorithm
                JSONObject primResult = PrimAlgorithm.runPrim(nodes, edgesArray);

                // Build ordered result object
                Map<String, Object> graphResult = new LinkedHashMap<>();
                graphResult.put("graph_id", graphId);
                graphResult.put("input_stats", inputStats);
                graphResult.put("prim", primResult);
                graphResult.put("kruskal", new LinkedHashMap<>()); // placeholder

                resultsList.add(graphResult);
            }

            // Wrap everything under "results"
            Map<String, Object> finalOutput = new LinkedHashMap<>();
            finalOutput.put("results", resultsList);

            // Use Gson for pretty printing, preserving order
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(finalOutput);

            // Write output file
            try (FileWriter file = new FileWriter("output_results.json")) {
                file.write(prettyJson);
                System.out.println("✅ Results written with correct key order and indentation!");
            }

        } catch (Exception e) {
            System.err.println("❌ Error processing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

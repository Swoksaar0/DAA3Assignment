package com.example.mst;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OutputGenerator {
    public static void main(String[] args) throws Exception {
        InputStream is = OutputGenerator.class.getClassLoader()
                .getResourceAsStream("assign_3_input.json");
        if (is == null) {
            is = new FileInputStream("assign_3_input.json");
        }
        JsonObject root = JsonParser.parseReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        ).getAsJsonObject();
        JsonArray graphs = root.getAsJsonArray("graphs");

        System.out.println("Warming up JVM...");
        for (int w = 0; w < 5; w++) {
            JsonObject go = graphs.get(0).getAsJsonObject();
            List<String> nodes = new ArrayList<>();
            JsonArray na = go.getAsJsonArray("nodes");
            for (int j = 0; j < na.size(); j++) {
                nodes.add(na.get(j).getAsString());
            }
            List<Edge> edges = new ArrayList<>();
            JsonArray ea = go.getAsJsonArray("edges");
            for (int j = 0; j < ea.size(); j++) {
                JsonObject eo = ea.get(j).getAsJsonObject();
                edges.add(new Edge(
                        eo.get("from").getAsString(),
                        eo.get("to").getAsString(),
                        eo.get("weight").getAsInt()
                ));
            }
            Graph g = new Graph(nodes, edges);
            Prim.run(g);
            Kruskal.run(g);
        }
        System.out.println("Warm-up complete. Running tests...\n");

        JsonArray results = new JsonArray();
        for (int i = 0; i < graphs.size(); i++) {
            JsonObject go = graphs.get(i).getAsJsonObject();
            int id = go.get("id").getAsInt();
            List<String> nodes = new ArrayList<>();
            JsonArray na = go.getAsJsonArray("nodes");
            for (int j = 0; j < na.size(); j++) {
                nodes.add(na.get(j).getAsString());
            }
            List<Edge> edges = new ArrayList<>();
            JsonArray ea = go.getAsJsonArray("edges");
            for (int j = 0; j < ea.size(); j++) {
                JsonObject eo = ea.get(j).getAsJsonObject();
                String u = eo.get("from").getAsString();
                String v = eo.get("to").getAsString();
                int w = eo.get("weight").getAsInt();
                edges.add(new Edge(u, v, w));
            }
            Graph graph = new Graph(nodes, edges);
            MSTResult rp = Prim.run(graph);
            MSTResult rk = Kruskal.run(graph);
            JsonObject rec = new JsonObject();
            rec.addProperty("graph_id", id);
            JsonObject inStats = new JsonObject();
            inStats.addProperty("vertices", nodes.size());
            inStats.addProperty("edges", edges.size());
            rec.add("input_stats", inStats);
            JsonObject p = new JsonObject();
            JsonArray pe = new JsonArray();
            for (int j = 0; j < rp.getMstEdges().size(); j++) {
                Edge e = rp.getMstEdges().get(j);
                JsonObject eo = new JsonObject();
                eo.addProperty("from", e.getU());
                eo.addProperty("to", e.getV());
                eo.addProperty("weight", e.getWeight());
                pe.add(eo);
            }
            p.add("mst_edges", pe);
            p.addProperty("total_cost", rp.getTotalCost());
            p.addProperty("operations_count", rp.getOperationCount());
            p.addProperty("execution_time_ms",
                    Math.round(rp.getExecutionTimeMs() * 100.0) / 100.0);
            rec.add("prim", p);
            JsonObject kObj = new JsonObject();
            JsonArray ke = new JsonArray();
            for (int j = 0; j < rk.getMstEdges().size(); j++) {
                Edge e = rk.getMstEdges().get(j);
                JsonObject eo = new JsonObject();
                eo.addProperty("from", e.getU());
                eo.addProperty("to", e.getV());
                eo.addProperty("weight", e.getWeight());
                ke.add(eo);
            }
            kObj.add("mst_edges", ke);
            kObj.addProperty("total_cost", rk.getTotalCost());
            kObj.addProperty("operations_count", rk.getOperationCount());
            kObj.addProperty("execution_time_ms",
                    Math.round(rk.getExecutionTimeMs() * 100.0) / 100.0);
            rec.add("kruskal", kObj);
            results.add(rec);

            System.out.printf("Graph %d: Prim %.2fms, Kruskal %.2fms%n",
                    id, rp.getExecutionTimeMs(), rk.getExecutionTimeMs());
        }
        JsonObject out = new JsonObject();
        out.add("results", results);
        Writer w = new OutputStreamWriter(
                new FileOutputStream("assign_3_output.json"),
                StandardCharsets.UTF_8
        );
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(out, w);
        w.close();
        System.out.println("\n✓ assign_3_output.json generated");
    }
}
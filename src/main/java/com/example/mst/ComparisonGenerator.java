package com.example.mst;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ComparisonGenerator {
    public static void main(String[] args) throws Exception {
        InputStream is = ComparisonGenerator.class.getClassLoader()
                .getResourceAsStream("assign_3_input.json");
        if (is == null) {
            is = new FileInputStream("assign_3_input.json");
        }
        JsonObject root = JsonParser.parseReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        ).getAsJsonObject();
        JsonArray graphs = root.getAsJsonArray("graphs");

        System.out.println("Warming up JVM...");
        JsonObject go0 = graphs.get(0).getAsJsonObject();
        for (int w = 0; w < 5; w++) {
            List<String> nodes = new ArrayList<>();
            JsonArray na = go0.getAsJsonArray("nodes");
            for (int j = 0; j < na.size(); j++) {
                nodes.add(na.get(j).getAsString());
            }
            List<Edge> edges = new ArrayList<>();
            JsonArray ea = go0.getAsJsonArray("edges");
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
        System.out.println("Warm-up complete.\n");

        PrintWriter writer = new PrintWriter(new FileWriter("results_comparison.csv"));
        writer.println("graph_id;vertices;edges;algorithm;cost;operations;time_ms");

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
                edges.add(new Edge(
                        eo.get("from").getAsString(),
                        eo.get("to").getAsString(),
                        eo.get("weight").getAsInt()
                ));
            }
            Graph graph = new Graph(nodes, edges);
            MSTResult prim = Prim.run(graph);
            MSTResult kruskal = Kruskal.run(graph);

            writer.printf("%d;%d;%d;Prim;%d;%d;%.2f%n",
                    id, nodes.size(), edges.size(),
                    prim.getTotalCost(), prim.getOperationCount(),
                    prim.getExecutionTimeMs());
            writer.printf("%d;%d;%d;Kruskal;%d;%d;%.2f%n",
                    id, nodes.size(), edges.size(),
                    kruskal.getTotalCost(), kruskal.getOperationCount(),
                    kruskal.getExecutionTimeMs());

            System.out.printf("Graph %d done%n", id);
        }
        writer.close();
        System.out.println("\n✓ results_comparison.csv created");
    }
}
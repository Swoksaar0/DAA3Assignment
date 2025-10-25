package com.example.mst;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("assign_3_input.json");
        if (is == null) {
            is = new FileInputStream("assign_3_input.json");
        }
        JsonObject root = JsonParser.parseReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        ).getAsJsonObject();
        JsonArray graphs = root.getAsJsonArray("graphs");
        for (int i = 0; i < graphs.size(); i++) {
            JsonObject go = graphs.get(i).getAsJsonObject();
            int id = go.get("id").getAsInt();
            List<String> nodes = new ArrayList<String>();
            JsonArray na = go.getAsJsonArray("nodes");
            for (int j = 0; j < na.size(); j++) {
                nodes.add(na.get(j).getAsString());
            }
            List<Edge> edges = new ArrayList<Edge>();
            JsonArray ea = go.getAsJsonArray("edges");
            for (int j = 0; j < ea.size(); j++) {
                JsonObject eo = ea.get(j).getAsJsonObject();
                String u = eo.get("from").getAsString();
                String v = eo.get("to").getAsString();
                int w = eo.get("weight").getAsInt();
                edges.add(new Edge(u, v, w));
            }
            Graph graph = new Graph(nodes, edges);
            MSTResult p = Prim.run(graph);
            MSTResult k = Kruskal.run(graph);
            System.out.println("Graph " + id);
            System.out.println("  Prim   cost=" + p.getTotalCost()
                    + " ops=" + p.getOperationCount()
                    + " time=" + String.format("%.3f", p.getExecutionTimeMs()) + "ms"
                    + " edges=" + p.getMstEdges());
            System.out.println("  Kruskal cost=" + k.getTotalCost()
                    + " ops=" + k.getOperationCount()
                    + " time=" + String.format("%.3f", k.getExecutionTimeMs()) + "ms"
                    + " edges=" + k.getMstEdges());
        }
    }
}
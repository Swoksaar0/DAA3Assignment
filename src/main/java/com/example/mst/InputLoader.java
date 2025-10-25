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

public class InputLoader {
    private JsonArray graphs;

    public InputLoader() throws Exception {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("assign_3_input.json");
        if (is == null) {
            is = new FileInputStream("assign_3_input.json");
        }
        InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
        graphs = root.getAsJsonArray("graphs");
        reader.close();
        is.close();
    }

    public Graph loadGraph(int id) {
        for (int i = 0; i < graphs.size(); i++) {
            JsonObject go = graphs.get(i).getAsJsonObject();
            if (go.get("id").getAsInt() == id) {
                JsonArray na = go.getAsJsonArray("nodes");
                List<String> nodes = new ArrayList<String>();
                for (int j = 0; j < na.size(); j++) {
                    nodes.add(na.get(j).getAsString());
                }
                JsonArray ea = go.getAsJsonArray("edges");
                List<Edge> edges = new ArrayList<Edge>();
                for (int j = 0; j < ea.size(); j++) {
                    JsonObject eo = ea.get(j).getAsJsonObject();
                    edges.add(new Edge(
                            eo.get("from").getAsString(),
                            eo.get("to").getAsString(),
                            eo.get("weight").getAsInt()
                    ));
                }
                return new Graph(nodes, edges);
            }
        }
        throw new IllegalArgumentException("Graph id not found: " + id);
    }
}
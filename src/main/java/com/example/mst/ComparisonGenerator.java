package com.example.mst;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class ComparisonGenerator {
    public static void main(String[] args) throws Exception {
        InputStream is = ComparisonGenerator.class
                .getClassLoader()
                .getResourceAsStream("assign_3_output.json");
        if (is == null) {
            is = new FileInputStream("assign_3_output.json");
        }
        InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray resultsArray = root.getAsJsonArray("results");

        FileOutputStream fos = new FileOutputStream("comparison.csv");
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(osw);
        writer.write('\uFEFF');

        writer.write("graph_id;vertices;edges;cost_prim;ops_prim;time_prim;cost_kruskal;ops_kruskal;time_kruskal");
        writer.newLine();

        for (int i = 0; i < resultsArray.size(); i++) {
            JsonElement elem = resultsArray.get(i);
            JsonObject rec = elem.getAsJsonObject();

            int graphId = rec.get("graph_id").getAsInt();
            JsonObject stats = rec.getAsJsonObject("input_stats");
            int vertices = stats.get("vertices").getAsInt();
            int edges = stats.get("edges").getAsInt();

            JsonObject prim = rec.getAsJsonObject("prim");
            int costPrim = prim.get("total_cost").getAsInt();
            int opsPrim = prim.get("operations_count").getAsInt();
            double timePrim = prim.get("execution_time_ms").getAsDouble();

            JsonObject kruskal = rec.getAsJsonObject("kruskal");
            int costKruskal = kruskal.get("total_cost").getAsInt();
            int opsKruskal = kruskal.get("operations_count").getAsInt();
            double timeKruskal = kruskal.get("execution_time_ms").getAsDouble();

            String line = "";
            line = line + graphId;
            line = line + ";";
            line = line + vertices;
            line = line + ";";
            line = line + edges;
            line = line + ";";
            line = line + costPrim;
            line = line + ";";
            line = line + opsPrim;
            line = line + ";";
            line = line + timePrim;
            line = line + ";";
            line = line + costKruskal;
            line = line + ";";
            line = line + opsKruskal;
            line = line + ";";
            line = line + timeKruskal;

            writer.write(line);
            writer.newLine();
        }

        writer.close();
    }
}
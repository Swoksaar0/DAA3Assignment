
import com.example.mst.*;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsistencyTest {
    @Test
    public void testAll() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("assign_3_input.json");
        if (is == null) {
            is = new FileInputStream("assign_3_input.json");
        }
        JsonArray graphs = JsonParser.parseReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        ).getAsJsonObject().getAsJsonArray("graphs");
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
                edges.add(new Edge(
                        eo.get("from").getAsString(),
                        eo.get("to").getAsString(),
                        eo.get("weight").getAsInt()
                ));
            }
            Graph g = new Graph(nodes, edges);
            MSTResult rp = Prim.run(g);
            MSTResult rk = Kruskal.run(g);
            assertEquals(rp.getTotalCost(), rk.getTotalCost());
            assertEquals(nodes.size() - 1, rp.getMstEdges().size());
            assertEquals(nodes.size() - 1, rk.getMstEdges().size());
        }
    }
}
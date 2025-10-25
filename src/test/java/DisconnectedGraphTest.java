
import com.example.mst.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class DisconnectedGraphTest {
    @Test
    public void testDisconnectedGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C");
        List<Edge> edges = new ArrayList<Edge>();
        Graph g = new Graph(nodes, edges);

        MSTResult rPrim = Prim.run(g);
        MSTResult rKruskal = Kruskal.run(g);

        assertTrue(rPrim.getMstEdges().size() < nodes.size() - 1);
        assertTrue(rKruskal.getMstEdges().size() < nodes.size() - 1);
    }
}
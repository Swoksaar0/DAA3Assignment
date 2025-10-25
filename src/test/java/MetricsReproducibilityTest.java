
import com.example.mst.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class MetricsReproducibilityTest {
    @Test
    public void testMetricsNonNegativeAndReproducible() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(new Edge("A","B",1));
        edges.add(new Edge("B","C",2));
        edges.add(new Edge("C","D",3));
        Graph g = new Graph(nodes, edges);

        MSTResult p1 = Prim.run(g);
        MSTResult p2 = Prim.run(g);
        assertTrue(p1.getOperationCount() >= 0);
        assertTrue(p1.getExecutionTimeMs() >= 0.0);
        assertEquals(p1.getOperationCount(), p2.getOperationCount());
        assertEquals(p1.getTotalCost(), p2.getTotalCost());

        MSTResult k1 = Kruskal.run(g);
        MSTResult k2 = Kruskal.run(g);
        assertTrue(k1.getOperationCount() >= 0);
        assertTrue(k1.getExecutionTimeMs() >= 0.0);
        assertEquals(k1.getOperationCount(), k2.getOperationCount());
        assertEquals(k1.getTotalCost(), k2.getTotalCost());
    }
}
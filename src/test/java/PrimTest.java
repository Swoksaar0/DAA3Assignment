
import com.example.mst.Edge;
import com.example.mst.Graph;
import com.example.mst.MSTResult;
import com.example.mst.Prim;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PrimTest {
    @Test
    public void testSmallGraph() {
        List<String> nodes = Arrays.asList("A","B","C","D");
        List<Edge> edges = Arrays.asList(
                new Edge("A","B",1),
                new Edge("A","C",4),
                new Edge("B","C",2),
                new Edge("C","D",3),
                new Edge("B","D",5)
        );
        Graph g = new Graph(nodes, edges);
        MSTResult r = Prim.run(g);
        assertEquals(6, r.getTotalCost());
        assertEquals(nodes.size() - 1, r.getMstEdges().size());
    }

    @Test
    public void testDisconnectedGraph() {
        List<String> nodes = Arrays.asList("A","B","C");
        List<Edge> edges = Arrays.asList(
                new Edge("A","B",1)
        );
        Graph g = new Graph(nodes, edges);
        MSTResult r = Prim.run(g);
        assertTrue(r.getMstEdges().size() < nodes.size() - 1);
    }

    @Test
    public void testSelfLoop() {
        List<String> nodes = Arrays.asList("A","B");
        List<Edge> edges = Arrays.asList(
                new Edge("A","A",1),
                new Edge("A","B",2)
        );
        Graph g = new Graph(nodes, edges);
        MSTResult r = Prim.run(g);
        assertEquals(2, r.getTotalCost());
        assertEquals(1, r.getMstEdges().size());
    }

    @Test
    public void testNegativeWeight() {
        List<String> nodes = Arrays.asList("A","B");
        List<Edge> edges = Arrays.asList(
                new Edge("A","B",-1)
        );
        Graph g = new Graph(nodes, edges);
        assertThrows(IllegalArgumentException.class, () -> Prim.run(g));
    }

    @Test
    public void testAcyclicAndConnected() {
        List<String> nodes = Arrays.asList("A","B","C","D");
        List<Edge> edges = Arrays.asList(
                new Edge("A","B",1),
                new Edge("B","C",2),
                new Edge("C","D",3),
                new Edge("A","D",4)
        );
        Graph g = new Graph(nodes, edges);
        MSTResult r = Prim.run(g);
        assertEquals(nodes.size() - 1, r.getMstEdges().size());
        Map<String,List<String>> adj = new HashMap<>();
        for (String v : nodes) adj.put(v, new ArrayList<>());
        for (Edge e : r.getMstEdges()) {
            adj.get(e.getU()).add(e.getV());
            adj.get(e.getV()).add(e.getU());
        }
        Set<String> visited = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.add(nodes.get(0));
        visited.add(nodes.get(0));
        while (!queue.isEmpty()) {
            String u = queue.removeFirst();
            for (String w : adj.get(u)) {
                if (!visited.contains(w)) {
                    visited.add(w);
                    queue.addLast(w);
                }
            }
        }
        assertEquals(nodes.size(), visited.size());
    }

    @Test
    public void testExecutionTimeAndReproducibility() {
        List<String> nodes = Arrays.asList("A","B","C","D");
        List<Edge> edges = Arrays.asList(
                new Edge("A","B",1),
                new Edge("B","C",2),
                new Edge("C","D",3)
        );
        Graph g = new Graph(nodes, edges);
        MSTResult r1 = Prim.run(g);
        MSTResult r2 = Prim.run(g);
        assertTrue(r1.getExecutionTimeMs() >= 0.0);
        assertTrue(r1.getOperationCount() >= 0);
        assertEquals(r1.getTotalCost(), r2.getTotalCost());
        assertEquals(r1.getOperationCount(), r2.getOperationCount());
    }
}
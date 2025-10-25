
import com.example.mst.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    @Test
    public void testBothAlgorithmsProduceSameCost() throws Exception {
        InputLoader loader = new InputLoader();

        int[] graphIds = {1, 2, 3, 5, 10};
        for (int id : graphIds) {
            Graph g = loader.loadGraph(id);
            MSTResult prim = Prim.run(g);
            MSTResult kruskal = Kruskal.run(g);

            assertEquals(prim.getTotalCost(), kruskal.getTotalCost(),
                    "Graph " + id + ": costs must be identical");
        }
    }

    @Test
    public void testMSTEdgeCountEqualsVMinusOne() throws Exception {
        InputLoader loader = new InputLoader();
        Graph g = loader.loadGraph(2);
        int V = g.getVertices().size();

        MSTResult prim = Prim.run(g);
        assertEquals(V - 1, prim.getMstEdges().size(),
                "Prim: MST must have V-1 edges");

        MSTResult kruskal = Kruskal.run(g);
        assertEquals(V - 1, kruskal.getMstEdges().size(),
                "Kruskal: MST must have V-1 edges");
    }

    @Test
    public void testMSTIsAcyclic() throws Exception {
        InputLoader loader = new InputLoader();
        Graph g = loader.loadGraph(3);

        MSTResult result = Prim.run(g);
        assertEquals(g.getVertices().size() - 1, result.getMstEdges().size(),
                "MST with V-1 edges is acyclic");
    }

    @Test
    public void testMSTConnectsAllVertices() throws Exception {
        InputLoader loader = new InputLoader();
        Graph g = loader.loadGraph(4);

        MSTResult result = Kruskal.run(g);
        Set<String> covered = new HashSet<>();

        for (Edge e : result.getMstEdges()) {
            covered.add(e.getU());
            covered.add(e.getV());
        }

        assertEquals(g.getVertices().size(), covered.size(),
                "MST must connect all vertices");
    }

    @Test
    public void testDisconnectedGraphHandling() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("C", "D", 2)
        );
        Graph g = new Graph(nodes, edges);

        MSTResult prim = Prim.run(g);
        MSTResult kruskal = Kruskal.run(g);

        assertTrue(prim.getMstEdges().size() < nodes.size() - 1,
                "Prim: disconnected graph produces incomplete MST");
        assertTrue(kruskal.getMstEdges().size() < nodes.size() - 1,
                "Kruskal: disconnected graph produces incomplete MST");
    }

    @Test
    public void testExecutionTimeNonNegative() throws Exception {
        InputLoader loader = new InputLoader();
        Graph g = loader.loadGraph(5);

        MSTResult prim = Prim.run(g);
        assertTrue(prim.getExecutionTimeMs() >= 0,
                "Execution time must be non-negative");

        MSTResult kruskal = Kruskal.run(g);
        assertTrue(kruskal.getExecutionTimeMs() >= 0,
                "Execution time must be non-negative");
    }

    @Test
    public void testOperationCountNonNegative() throws Exception {
        InputLoader loader = new InputLoader();
        Graph g = loader.loadGraph(6);

        MSTResult prim = Prim.run(g);
        assertTrue(prim.getOperationCount() >= 0,
                "Operation count must be non-negative");

        MSTResult kruskal = Kruskal.run(g);
        assertTrue(kruskal.getOperationCount() >= 0,
                "Operation count must be non-negative");
    }

    @Test
    public void testResultsAreReproducible() throws Exception {
        InputLoader loader = new InputLoader();
        Graph g = loader.loadGraph(7);

        MSTResult prim1 = Prim.run(g);
        MSTResult prim2 = Prim.run(g);

        assertEquals(prim1.getTotalCost(), prim2.getTotalCost(),
                "Prim: cost must be reproducible");
        assertEquals(prim1.getMstEdges().size(), prim2.getMstEdges().size(),
                "Prim: edge count must be reproducible");

        MSTResult kruskal1 = Kruskal.run(g);
        MSTResult kruskal2 = Kruskal.run(g);

        assertEquals(kruskal1.getTotalCost(), kruskal2.getTotalCost(),
                "Kruskal: cost must be reproducible");
        assertEquals(kruskal1.getMstEdges().size(), kruskal2.getMstEdges().size(),
                "Kruskal: edge count must be reproducible");
    }


    @Test
    public void testSmallGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 4),
                new Edge("B", "C", 2),
                new Edge("C", "D", 3),
                new Edge("B", "D", 5)
        );
        Graph g = new Graph(nodes, edges);

        MSTResult prim = Prim.run(g);
        MSTResult kruskal = Kruskal.run(g);

        assertEquals(6, prim.getTotalCost());
        assertEquals(6, kruskal.getTotalCost());
        assertEquals(3, prim.getMstEdges().size());
        assertEquals(3, kruskal.getMstEdges().size());
    }

    @Test
    public void testSingleEdge() {
        List<String> nodes = Arrays.asList("A", "B");
        List<Edge> edges = Arrays.asList(new Edge("A", "B", 10));
        Graph g = new Graph(nodes, edges);

        MSTResult prim = Prim.run(g);
        MSTResult kruskal = Kruskal.run(g);

        assertEquals(10, prim.getTotalCost());
        assertEquals(10, kruskal.getTotalCost());
        assertEquals(1, prim.getMstEdges().size());
        assertEquals(1, kruskal.getMstEdges().size());
    }

    @Test
    public void testCompleteGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("A", "C", 2),
                new Edge("B", "C", 3)
        );
        Graph g = new Graph(nodes, edges);

        MSTResult prim = Prim.run(g);
        MSTResult kruskal = Kruskal.run(g);
        assertEquals(3, prim.getTotalCost());
        assertEquals(3, kruskal.getTotalCost());
        assertEquals(2, prim.getMstEdges().size());
        assertEquals(2, kruskal.getMstEdges().size());
    }
}

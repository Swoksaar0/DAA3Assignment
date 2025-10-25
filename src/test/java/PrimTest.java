
import com.example.mst.Edge;
import com.example.mst.Graph;
import com.example.mst.MSTResult;
import com.example.mst.Prim;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrimTest {
    @Test
    public void testSmall() {
        List<String> nodes = Arrays.asList("A","B","C","D");
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(new Edge("A","B",1));
        edges.add(new Edge("A","C",4));
        edges.add(new Edge("B","C",2));
        edges.add(new Edge("C","D",3));
        edges.add(new Edge("B","D",5));
        Graph g = new Graph(nodes, edges);
        MSTResult r = Prim.run(g);
        assertEquals(3, r.getMstEdges().size());
        assertEquals(6, r.getTotalCost());
    }
}
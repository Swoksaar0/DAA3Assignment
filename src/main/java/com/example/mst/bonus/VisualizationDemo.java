package com.example.mst.bonus;

import com.example.mst.*;

public class VisualizationDemo {
    public static void main(String[] args) throws Exception {
        InputLoader loader = new InputLoader();

        // Choose which graphs to visualize (small ones work best)
        int[] graphIds = {1, 2, 3}; // 5, 10, 15 vertices

        for (int id : graphIds) {
            System.out.println("\n=== Processing Graph " + id + " ===");
            Graph graph = loader.loadGraph(id);
            MSTResult prim = Prim.run(graph);
            MSTResult kruskal = Kruskal.run(graph);

            System.out.printf("Vertices: %d, Edges: %d%n",
                    graph.getVertices().size(), graph.getEdges().size());
            System.out.printf("Prim Cost: %d, Kruskal Cost: %d%n",
                    prim.getTotalCost(), kruskal.getTotalCost());

            // Save images (no GUI window needed)
            saveGraphImages(graph, prim, kruskal, id);
        }

        System.out.println("\n✓ All visualizations saved!");
        System.out.println("Check your project directory for PNG files.");
    }

    private static void saveGraphImages(Graph graph, MSTResult prim,
                                        MSTResult kruskal, int graphId) {
        // Create visualizers
        GraphVisualizer primViz = new GraphVisualizer(graph, prim,
                String.format("Prim's MST - Graph %d (Cost: %d)",
                        graphId, prim.getTotalCost()));

        GraphVisualizer kruskalViz = new GraphVisualizer(graph, kruskal,
                String.format("Kruskal's MST - Graph %d (Cost: %d)",
                        graphId, kruskal.getTotalCost()));

        // Save images
        primViz.saveImage(String.format("graph_%d_prim.png", graphId));
        kruskalViz.saveImage(String.format("graph_%d_kruskal.png", graphId));
    }

    // Optional: Show GUI window for one graph
    public static void showGUI(int graphId) throws Exception {
        InputLoader loader = new InputLoader();
        Graph graph = loader.loadGraph(graphId);
        MSTResult prim = Prim.run(graph);
        MSTResult kruskal = Kruskal.run(graph);

        GraphVisualizer.visualize(graph, prim, kruskal);
    }
}
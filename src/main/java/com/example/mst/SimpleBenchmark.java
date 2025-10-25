package com.example.mst;

public class SimpleBenchmark {
    public static void main(String[] args) throws Exception {
        InputLoader loader = new InputLoader();


        for (int i = 0; i < 3; i++) {
            Prim.run(loader.loadGraph(1));
            Kruskal.run(loader.loadGraph(1));
        }


        int[] graphIds = {1, 6, 16, 26};
        for (int id : graphIds) {
            Graph g = loader.loadGraph(id);

            long totalPrim = 0, totalKruskal = 0;
            int runs = 5;

            for (int i = 0; i < runs; i++) {
                long start = System.nanoTime();
                Prim.run(g);
                totalPrim += System.nanoTime() - start;

                start = System.nanoTime();
                Kruskal.run(g);
                totalKruskal += System.nanoTime() - start;
            }

            System.out.printf("Graph %d: Prim %.3f ms, Kruskal %.3f ms%n",
                    id, totalPrim / runs / 1e6, totalKruskal / runs / 1e6);
        }
    }
}
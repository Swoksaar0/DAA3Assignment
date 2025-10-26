package com.example.mst;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;

public class Prim {
    public static MSTResult run(Graph graph) {
        long start = System.nanoTime();
        List<String> vertices = graph.getVertices();
        int V = vertices.size();
        Set<String> visited = new HashSet<>();
        List<Edge> mst = new ArrayList<>();
        int opCount = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        String startV = vertices.get(0);
        visited.add(startV);
        List<Edge> adj0 = graph.getAdjacencyList().get(startV);
        for (Edge e : adj0) {
            pq.offer(e);
            opCount++;
        }

        while (!pq.isEmpty() && mst.size() < V - 1) {
            opCount++;
            Edge e = pq.poll();
            String u = e.getU();
            String v = e.getV();
            boolean uVis = visited.contains(u);
            boolean vVis = visited.contains(v);
            if (uVis && vVis) {
                continue;
            }
            String next = uVis ? v : u;
            visited.add(next);
            mst.add(e);
            List<Edge> adjNext = graph.getAdjacencyList().get(next);
            for (Edge ne : adjNext) {
                String other = ne.getOther(next);
                if (!visited.contains(other)) {
                    pq.offer(ne);
                    opCount++;
                }
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1000000.0;
        int totalCost = 0;
        for (Edge e : mst) {
            totalCost += e.getWeight();
        }
        return new MSTResult(mst, totalCost, opCount, timeMs);
    }
}
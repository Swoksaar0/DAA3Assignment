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
        Set<String> visited = new HashSet<String>();
        List<Edge> mst = new ArrayList<Edge>();
        int opCount = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>(new Comparator<Edge>() {
            public int compare(Edge e1, Edge e2) {
                if (e1.getWeight() < e2.getWeight()) {
                    return -1;
                } else {
                    if (e1.getWeight() > e2.getWeight()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        String startV = vertices.get(0);
        visited.add(startV);
        List<Edge> adj0 = graph.getAdjacencyList().get(startV);
        for (int i = 0; i < adj0.size(); i++) {
            pq.offer(adj0.get(i));
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
            String next;
            if (uVis) {
                next = v;
            } else {
                next = u;
            }
            visited.add(next);
            mst.add(e);
            List<Edge> adjNext = graph.getAdjacencyList().get(next);
            for (int i = 0; i < adjNext.size(); i++) {
                Edge ne = adjNext.get(i);
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
        for (int i = 0; i < mst.size(); i++) {
            totalCost += mst.get(i).getWeight();
        }
        return new MSTResult(mst, totalCost, opCount, timeMs);
    }
}
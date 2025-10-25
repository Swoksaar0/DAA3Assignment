package com.example.mst;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kruskal {
    public static MSTResult run(Graph graph) {
        long start = System.nanoTime();

        List<String> verts = graph.getVertices();
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < verts.size(); i++) {
            idx.put(verts.get(i), i);
        }

        List<Edge> allEdges = new ArrayList<>(graph.getEdges());

        int sortOps = 0;
        if (allEdges.size() > 0) {
            sortOps = (int)(allEdges.size() * Math.log(allEdges.size()) / Math.log(2));
        }
        allEdges.sort(Comparator.comparingInt(Edge::getWeight));

        DSU dsu = new DSU(verts.size());
        List<Edge> mst = new ArrayList<>();
        int cost = 0;
        int edgeOps = 0;

        for (Edge e : allEdges) {
            edgeOps++;
            int u = idx.get(e.getU());
            int v = idx.get(e.getV());
            if (dsu.union(u, v)) {
                mst.add(e);
                cost += e.getWeight();
                if (mst.size() == verts.size() - 1) {
                    break;
                }
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1000000.0;

        int ops = sortOps + dsu.getOpCount() + edgeOps;

        return new MSTResult(mst, cost, ops, timeMs);
    }
}
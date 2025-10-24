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
        Map<String, Integer> idx = new HashMap<String, Integer>();
        for (int i = 0; i < verts.size(); i++) {
            idx.put(verts.get(i), i);
        }
        List<Edge> allEdges = new ArrayList<Edge>();
        List<Edge> fromGraph = graph.getEdges();
        for (int i = 0; i < fromGraph.size(); i++) {
            allEdges.add(fromGraph.get(i));
        }
        for (int i = 0; i < allEdges.size() - 1; i++) {
            for (int j = 0; j < allEdges.size() - 1 - i; j++) {
                Edge e1 = allEdges.get(j);
                Edge e2 = allEdges.get(j + 1);
                if (e1.getWeight() > e2.getWeight()) {
                    allEdges.set(j, e2);
                    allEdges.set(j + 1, e1);
                }
            }
        }
        DSU dsu = new DSU(verts.size());
        List<Edge> mst = new ArrayList<Edge>();
        int cost = 0;
        for (int i = 0; i < allEdges.size(); i++) {
            Edge e = allEdges.get(i);
            int u = idx.get(e.getU());
            int v = idx.get(e.getV());
            if (dsu.union(u, v)) {
                mst.add(e);
                cost = cost + e.getWeight();
                if (mst.size() == verts.size() - 1) {
                    break;
                }
            }
        }
        long end = System.nanoTime();
        double timeMs = (end - start) / 1000000.0;
        int ops = dsu.getOpCount() + allEdges.size();
        return new MSTResult(mst, cost, ops, timeMs);
    }
}
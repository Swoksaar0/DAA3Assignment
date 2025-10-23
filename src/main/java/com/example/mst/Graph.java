package com.example.mst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private List<String> vertices;
    private List<Edge> edges;
    private Map<String, List<Edge>> adj;

    public Graph(List<String> vertices, List<Edge> edges) {
        this.vertices = new ArrayList<String>();
        for (String v : vertices) {
            this.vertices.add(v);
        }
        this.edges = new ArrayList<Edge>();
        for (Edge e : edges) {
            this.edges.add(e);
        }
        this.adj = new HashMap<String, List<Edge>>();
        for (int i = 0; i < this.vertices.size(); i++) {
            String v = this.vertices.get(i);
            this.adj.put(v, new ArrayList<Edge>());
        }
        for (int i = 0; i < this.edges.size(); i++) {
            Edge e = this.edges.get(i);
            String u = e.getU();
            String v = e.getV();
            List<Edge> listU = this.adj.get(u);
            List<Edge> listV = this.adj.get(v);
            listU.add(e);
            listV.add(e);
        }
    }

    public List<String> getVertices() {
        return Collections.unmodifiableList(this.vertices);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(this.edges);
    }

    public Map<String, List<Edge>> getAdjacencyList() {
        return this.adj;
    }
}
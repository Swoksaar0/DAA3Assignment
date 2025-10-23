package com.example.mst;

public class Edge {
    private final String u;
    private final String v;
    private final int weight;

    public Edge(String u, String v, int weight) {
        this.u = u; this.v = v; this.weight = weight;
    }

    public String getU() { return u; }
    public String getV() { return v; }
    public int getWeight() { return weight; }

    public String getOther(String x) {
        if (x.equals(u)) return v;
        if (x.equals(v)) return u;
        throw new IllegalArgumentException();
    }
}
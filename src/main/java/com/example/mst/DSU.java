package com.example.mst;

public class DSU {
    private int[] parent;
    private int[] rank;
    private int opCount;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
        opCount = 0;
    }

    public int getOpCount() {
        return this.opCount;
    }

    public int find(int x) {
        opCount++;
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public boolean union(int a, int b) {
        opCount++;
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return false;
        }
        if (rank[ra] < rank[rb]) {
            parent[ra] = rb;
        } else {
            parent[rb] = ra;
            if (rank[ra] == rank[rb]) {
                rank[ra] = rank[ra] + 1;
            }
        }
        return true;
    }
}
package com.example.mst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MSTResult {
    private List<Edge> mstEdges;
    private int totalCost;
    private int operationCount;
    private double executionTimeMs;

    public MSTResult(List<Edge> mstEdges, int totalCost, int operationCount, double executionTimeMs) {
        this.mstEdges = new ArrayList<Edge>();
        for (Edge e : mstEdges) {
            this.mstEdges.add(e);
        }
        this.totalCost = totalCost;
        this.operationCount = operationCount;
        this.executionTimeMs = executionTimeMs;
    }

    public List<Edge> getMstEdges() {
        return Collections.unmodifiableList(this.mstEdges);
    }

    public int getTotalCost() {
        return this.totalCost;
    }

    public int getOperationCount() {
        return this.operationCount;
    }

    public double getExecutionTimeMs() {
        return this.executionTimeMs;
    }
}
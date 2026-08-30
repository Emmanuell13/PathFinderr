/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.models;

/**
 *
 * @author HP
 */

import com.mycompany.pathfinder.model.enums.AlgorithmType;

public class AlgoRun {

    private int runId;
    private int gridId;
    private AlgorithmType algorithm;
    private double executionTime;
    private int cellsExplored;
    private int pathLength;

    public AlgoRun() {
    }

    public AlgoRun(int runId, int gridId, AlgorithmType algorithm,
                   double executionTime, int cellsExplored, int pathLength) {
        this.runId = runId;
        this.gridId = gridId;
        this.algorithm = algorithm;
        this.executionTime = executionTime;
        this.cellsExplored = cellsExplored;
        this.pathLength = pathLength;
    }

    public AlgoRun(int gridId, AlgorithmType algorithm,
                   double executionTime, int cellsExplored, int pathLength) {
        this.gridId = gridId;
        this.algorithm = algorithm;
        this.executionTime = executionTime;
        this.cellsExplored = cellsExplored;
        this.pathLength = pathLength;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public int getCellsExplored() {
        return cellsExplored;
    }

    public void setCellsExplored(int cellsExplored) {
        this.cellsExplored = cellsExplored;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    @Override
    public String toString() {
        return "AlgoRun{" +
                "runId=" + runId +
                ", gridId=" + gridId +
                ", algorithm=" + algorithm +
                ", executionTime=" + executionTime +
                ", cellsExplored=" + cellsExplored +
                ", pathLength=" + pathLength +
                '}';
    }
}

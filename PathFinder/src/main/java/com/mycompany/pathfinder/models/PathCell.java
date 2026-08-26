/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.models;

/**
 *
 * @author HP
 */

import com.mycompany.pathfinder.model.enums.CellState;

public class PathCell {

    private int pathCellId;
    private int runId;
    private int rowIndex;
    private int colIndex;
    private CellState cellState;

    public PathCell() {
    }

    public PathCell(int pathCellId, int runId, int rowIndex,
                    int colIndex, CellState cellState) {
        this.pathCellId = pathCellId;
        this.runId = runId;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellState = cellState;
    }

    public PathCell(int runId, int rowIndex, int colIndex, CellState cellState) {
        this.runId = runId;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellState = cellState;
    }

    public int getPathCellId() {
        return pathCellId;
    }

    public void setPathCellId(int pathCellId) {
        this.pathCellId = pathCellId;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public CellState getCellState() {
        return cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    @Override
    public String toString() {
        return "PathCell{" +
                "pathCellId=" + pathCellId +
                ", runId=" + runId +
                ", rowIndex=" + rowIndex +
                ", colIndex=" + colIndex +
                ", cellState=" + cellState +
                '}';
    }
}
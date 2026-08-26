/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.models;

/**
 *
 * @author HP
 */
import java.time.LocalDateTime;

public class Grid {

    private int gridId;
    private int rowsGrid;
    private int colsGrid;
    private LocalDateTime createdAt;

    public Grid() {
    }

    public Grid(int gridId, int rowsGrid, int colsGrid, LocalDateTime createdAt) {
        this.gridId = gridId;
        this.rowsGrid = rowsGrid;
        this.colsGrid = colsGrid;
        this.createdAt = createdAt;
    }

    public Grid(int rowsGrid, int colsGrid) {
        this.rowsGrid = rowsGrid;
        this.colsGrid = colsGrid;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
    }

    public int getRowsGrid() {
        return rowsGrid;
    }

    public void setRowsGrid(int rowsGrid) {
        this.rowsGrid = rowsGrid;
    }

    public int getColsGrid() {
        return colsGrid;
    }

    public void setColsGrid(int colsGrid) {
        this.colsGrid = colsGrid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "gridId=" + gridId +
                ", rowsGrid=" + rowsGrid +
                ", colsGrid=" + colsGrid +
                ", createdAt=" + createdAt +
                '}';
    }
}
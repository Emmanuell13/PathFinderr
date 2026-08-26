/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.models;

/**
 *
 * @author HP
 */

import com.mycompany.pathfinder.model.enums.CellType;

public class GridCell {

    private int cellId;
    private int gridId;
    private int rowIndex;
    private int colIndex;
    private CellType cellType;

    public GridCell() {
    }

    public GridCell(int cellId, int gridId, int rowIndex, int colIndex, CellType cellType) {
        this.cellId = cellId;
        this.gridId = gridId;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellType = cellType;
    }

    public GridCell(int gridId, int rowIndex, int colIndex, CellType cellType) {
        this.gridId = gridId;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.cellType = cellType;
    }

    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
    }

    public int getGridId() {
        return gridId;
    }

    public void setGridId(int gridId) {
        this.gridId = gridId;
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

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    @Override
    public String toString() {
        return "GridCell{" +
                "cellId=" + cellId +
                ", gridId=" + gridId +
                ", rowIndex=" + rowIndex +
                ", colIndex=" + colIndex +
                ", cellType=" + cellType +
                '}';
    }
}
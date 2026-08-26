/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.grid;

/**
 *
 * @author Admin
 */

public class Cell {

    private final int row;
    private final int column;
    private CellState state;

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.state = CellState.EMPTY;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isWall() {
        return state == CellState.WALL;
    }

    public void toggleWall() {
        if (state == CellState.EMPTY) {
            state = CellState.WALL;
        } else if (state == CellState.WALL) {
            state = CellState.EMPTY;
        }
    }
}
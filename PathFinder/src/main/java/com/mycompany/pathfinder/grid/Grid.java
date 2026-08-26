/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.grid;

/**
 *
 * @author Admin
 */

public class Grid {

    public static final int ROWS = 20;
    public static final int COLUMNS = 25;

    private final Cell[][] cells;

    public Grid() {
        cells = new Cell[ROWS][COLUMNS];

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                cells[row][column] = new Cell(row, column);
            }
        }
    }

    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

    public Cell[][] getCells() {
        return cells;
    }
}

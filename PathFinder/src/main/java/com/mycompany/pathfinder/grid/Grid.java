/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.grid;

/**
 *
 * @author Admin
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public void generateRandomWalls() {

        Random random = new Random();

        double wallProbability = 0.10;

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {

                if (random.nextDouble() < wallProbability) {
                    cells[row][column].setState(CellState.WALL);
                }
            }
        }
    }
     public List<Cell> getNeighbors(Cell cell) {

        List<Cell> neighbors = new ArrayList<>();

        int row = cell.getRow();
        int column = cell.getColumn();

        // Haut
        if (row > 0) {
            neighbors.add(cells[row - 1][column]);
        }

        // Bas
        if (row < ROWS - 1) {
            neighbors.add(cells[row + 1][column]);
        }

        // Gauche
        if (column > 0) {
            neighbors.add(cells[row][column - 1]);
        }

        // Droite
        if (column < COLUMNS - 1) {
            neighbors.add(cells[row][column + 1]);
        }

        return neighbors;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.algorithms;

import com.mycompany.pathfinder.grid.Cell;

/**
 *
 * @author Haidar
 */
public class CellDistance {
    
    private final Cell cell;
    private final int distance;

    public CellDistance(Cell cell, int distance) {
        this.cell = cell;
        this.distance = distance;
    }

    public Cell getCell() {
        return cell;
    }

    public int getDistance() {
        return distance;
    }
    
}

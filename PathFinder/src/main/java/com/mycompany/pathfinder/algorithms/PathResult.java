/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.algorithms;

import com.mycompany.pathfinder.grid.Cell;
import java.util.List;

/**
 *
 * @author Haidar
 */
public class PathResult {
    
    private final List<Cell> exploredCells;
    private final List<Cell> path;
    private final double executionTime;
    
    public PathResult(List<Cell> exploredCells, List<Cell> path, double executionTime) {
       this.exploredCells = exploredCells;
       this.path = path;
       this.executionTime = executionTime;
    }
    
    public List<Cell> getExploredCells() {
        return exploredCells;
    }

    public List<Cell> getPath() {
        return path;
    }

    public double getExecutionTime() {
        return executionTime;
    }
    
}

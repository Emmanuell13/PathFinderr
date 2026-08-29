/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.algorithms;

import com.mycompany.pathfinder.grid.Cell;
import com.mycompany.pathfinder.grid.CellState;
import com.mycompany.pathfinder.grid.Grid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Haidar
 */
public class BFS implements PathFinderAlgorithm {
    
    @Override
    public PathResult findPath(Grid grid) {
        
        long startTime = System.nanoTime();
        
        Queue<Cell> queue = new LinkedList<>();
        
        Set<Cell> visited = new HashSet<>();
        
        Map<Cell, Cell> parent = new HashMap<>();
        
        List<Cell> exploredCells = new ArrayList<>();
        
        Cell start = grid.getStart();
        Cell end = grid.getEnd();
        
        if(start == null || end == null) {
            return new PathResult(exploredCells, new ArrayList<>(), 0);
        }
        
        queue.add(start);
        visited.add(start);
        
        while(!queue.isEmpty()) {
            
            Cell current = queue.poll();
            exploredCells.add(current);
            
            if(current == end) {
                break;
            }
            
            List<Cell> neighbours = grid.getNeighbors(current);
            
            for(Cell neighbour : neighbours) {
                
                if (neighbour.getState() == CellState.WALL) {
                    continue;
                }
                
                if (visited.contains(neighbour)) {
                    continue;
                }
                
                visited.add(neighbour);
                
                parent.put(neighbour, current);
                
                queue.add(neighbour);
                
            }
        }
        
        List<Cell> path = reconstructPath(parent, start, end);
        
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000.0;

        return new PathResult(exploredCells, path, executionTime);
        
    }
    
    private List<Cell> reconstructPath(Map<Cell, Cell> parent, Cell start, Cell end) {

        List<Cell> path = new ArrayList<>();

        if (!start.equals(end) && !parent.containsKey(end)) {
            return path;
        }

        Cell current = end;

        while (current != null) {

            path.add(current);

            if (current.equals(start)) {
                break;
            }

            current = parent.get(current);
        }

        Collections.reverse(path);

        return path;
    }
}
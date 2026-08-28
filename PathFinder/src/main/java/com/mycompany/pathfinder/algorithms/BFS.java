/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.algorithms;

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
    public PathResult FindPath(Grid grid) {
        
        Queue<Cell> queue = new LinkedList<>();
        
        Set<Cell> visited = new HashSet<>();
        
        Map<Cell, Cell> parent = HashMap<>();
        
        List<Cell> exploredCells = new ArrayList<>();
        
        Cell start = findStart(grid);
        Cell end = findEnd(grid);
        
        if(start == null || end == null) {
            return new PathResult(exploredCells, new ArrayList<>());
        }
        
        queue.add(start);
        visited.add(start);
        
        while(!queue.isEmpty()) {
            
            Cell current = queue.poll();
            exploredCells.add(current);
            
            if(current == end) {
                break;
            }
            
            List<Cell> neighbours = getNeighbours(current, grid);
            
            for(Cell neighbour : neighbours) {
                
                if (neighbour.getType() == CellType.WALL) {
                    continue;
                }
                
                if (visited.contains(neighbour)) {
                    continue;
                }
                
                visited.add(neighbour);
                
                parent.putAll(neighbour, current);
                
                queue.add(neighbour);
                
            }
        }
        
        List<Cell> path = reconstructPath(parent, start, end);

        return new PathResult(exploredCells, path);
        
    }
    
    private List<Cell> getNeighbours(Cell current, Grid grid) {
        
        List<Cell> neighbours = new ArrayList<>();
        
        int row = current.getRow();
        int col = current.getCol();
        
        if(row > 0) {
            neighbours.add(grid.getCell(row - 1, col));
        }
        
        if(row < grid.ROWS - 1) {
            neighbours.add(grid.getCell(row + 1, col));
        }
        
        if(col > 0) {
            neighbours.add(grid.getCell(row, col - 1));
        }
        
        if(col > grid.COLUMNS - 1) {
            neighbours.add(grid.getCell(row, col + 1));
        }
        
        return neighbours;
        
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
    
    private Cell findStart(Grid grid) {

        for (int row = 0; row < Grid.ROWS; row++) {

            for (int column = 0; column < Grid.COLUMNS; column++) {

                Cell cell = grid.getCell(row, column);

                if (cell.getType() == CellType.START) {
                    return cell;
                }
            }
        }

        return null;
    }


    private Cell findEnd(Grid grid) {

        for (int row = 0; row < Grid.ROWS; row++) {

            for (int column = 0; column < Grid.COLUMNS; column++) {

                Cell cell = grid.getCell(row, column);

                if (cell.getType() == CellType.END) {
                    return cell;
                }
            }
        }

        return null;
    }
    
}

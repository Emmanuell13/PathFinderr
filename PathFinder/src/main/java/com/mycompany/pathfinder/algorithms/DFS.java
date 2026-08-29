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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author Haidar
 */
public class DFS implements PathFinderAlgorithm {
    
    @Override
    public PathResult findPath(Grid grid) {
        
        List<Cell> exploredCells = new ArrayList<>();

        Stack<Cell> stack = new Stack<>();

        Set<Cell> visited = new HashSet<>();

        Map<Cell, Cell> parent = new HashMap<>();

        Cell start = grid.findStart(grid);
        Cell end = grid.findEnd(grid);

        if (start == null || end == null) {
            return new PathResult(exploredCells, new ArrayList<>(), 0);
        }

        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {

            Cell current = stack.pop();

            exploredCells.add(current);

            if (current.equals(end)) {
                break;
            }

            List<Cell> neighbours = grid.getNeighbors(current);

            for (Cell neighbour : neighbours) {

                if (neighbour.getState() == CellState.WALL) {
                    continue;
                }

                if (visited.contains(neighbour)) {
                    continue;
                }

                visited.add(neighbour);

                parent.put(neighbour, current);

                stack.push(neighbour);
            }
        }

        List<Cell> path = reconstructPath(parent, start, end);

        return new PathResult(exploredCells, path, 0);
    }

    private List<Cell> reconstructPath(Map<Cell, Cell> parent, Cell start,Cell end) {

        List<Cell> path = new ArrayList<>();

        if (!start.equals(end)&& !parent.containsKey(end)) {
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

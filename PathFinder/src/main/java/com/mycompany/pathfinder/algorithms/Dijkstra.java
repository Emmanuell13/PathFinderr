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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
/**
 *
 * @author Haidar
 */
public class Dijkstra implements PathFinderAlgorithm {
    
    @Override
    public PathResult findPath(Grid grid) {
        
        List<Cell> exploredCells = new ArrayList<>();
        List<Cell> path = new ArrayList<>();

        Cell start = grid.getStart(grid);
        Cell end = grid.getEnd();

        if (start == null || end == null) {
            return new PathResult(exploredCells, path, 0);
        }

        Map<Cell, Integer> distances = new HashMap<>();

        Map<Cell, Cell> parent = new HashMap<>();

        Set<Cell> visited = new HashSet<>();

        PriorityQueue<CellDistance> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(CellDistance::getDistance));

        distances.put(start, 0);

        priorityQueue.add(new CellDistance(start, 0));

        while (!priorityQueue.isEmpty()) {

            CellDistance currentEntry = priorityQueue.poll();

            Cell current = currentEntry.getCell();

            int currentDistance = currentEntry.getDistance();

            if (currentDistance != distances.get(current)) {
                continue;
            }

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);

            exploredCells.add(current);

            if (current.equals(end)) {
                break;
            }

            for (Cell neighbour : grid.getNeighbors(current, grid)) {

                if (neighbour.getState() == CellState.WALL) {
                    continue;
                }

                if (visited.contains(neighbour)) {
                    continue;
                }

                int newDistance = currentDistance + 1;

                int oldDistance = distances.getOrDefault(neighbour, Integer.MAX_VALUE);

                if (newDistance < oldDistance) {

                    distances.put(neighbour, newDistance);

                    parent.put(neighbour, current);

                    priorityQueue.add(new CellDistance(neighbour, newDistance));
                }
            }
        }

        path = reconstructPath(parent, start, end);

        return new PathResult(
                exploredCells,
                path,
                0
        );
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

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

public class AStar implements PathFinderAlgorithm {

    @Override
    public PathResult findPath(Grid grid) {

        List<Cell> exploredCells = new ArrayList<>();
        List<Cell> path = new ArrayList<>();

        Cell start = grid.getStart();
        Cell end = grid.getEnd();

        if (start == null || end == null) {
            return new PathResult(exploredCells, path, 0);
        }

        Map<Cell, Integer> gScore = new HashMap<>();
Map<Cell, Cell> parent = new HashMap<>();

PriorityQueue<CellDistance> openSet =
        new PriorityQueue<>(
                Comparator.comparingInt(CellDistance::getDistance)
        );

gScore.put(start, 0);

int startHeuristic = heuristic(start, end);

openSet.add(
        new CellDistance(start, startHeuristic)
);

Set<Cell> closedSet = new HashSet<>();

while (!openSet.isEmpty()) {

    CellDistance currentEntry = openSet.poll();

    Cell current = currentEntry.getCell();

    if (closedSet.contains(current)) {
        continue;
    }

    closedSet.add(current);

    exploredCells.add(current);

    if (current.equals(end)) {
        break;
    }
    
    for (Cell neighbour : grid.getNeighbors(current)) {

    if (neighbour.getState() == CellState.WALL) {
        continue;
    }

    if (closedSet.contains(neighbour)) {
        continue;
    }

    int tentativeGScore =
            gScore.get(current) + 1;

    int currentGScore =
            gScore.getOrDefault(
                    neighbour,
                    Integer.MAX_VALUE
            );

    if (tentativeGScore < currentGScore) {

        parent.put(neighbour, current);

        gScore.put(
                neighbour,
                tentativeGScore
        );

        int hScore = heuristic(neighbour, end);

        int fScore =
                tentativeGScore + hScore;

        openSet.add(
                new CellDistance(
                        neighbour,
                        fScore
                )
        );
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
    
    private List<Cell> reconstructPath(
        Map<Cell, Cell> parent,
        Cell start,
        Cell end) {

    List<Cell> path = new ArrayList<>();

    // Aucun chemin trouvé
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
    
    private int heuristic(Cell cell, Cell end) {

    return Math.abs(cell.getRow() - end.getRow())
            + Math.abs(cell.getColumn() - end.getColumn());
}
}

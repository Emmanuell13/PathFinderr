/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder;

/**
 *
 * @author Admin
 */

import com.mycompany.pathfinder.grid.AlgorithmType;
import com.mycompany.pathfinder.grid.Cell;
import com.mycompany.pathfinder.grid.CellState;
import com.mycompany.pathfinder.grid.Grid;

import com.mycompany.pathfinder.algorithms.BFS;
import com.mycompany.pathfinder.algorithms.DFS;
import com.mycompany.pathfinder.algorithms.Dijkstra;
import com.mycompany.pathfinder.algorithms.AStar;
import com.mycompany.pathfinder.algorithms.PathFinderAlgorithm;
import com.mycompany.pathfinder.algorithms.PathResult;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class MainController {

    @FXML
    private GridPane gridPane;

    @FXML
    private Button startButton;
    @FXML
    private Button endButton;
    @FXML
    private Button launchButton;
    @FXML
    private Button resetButton;
    
    @FXML
    private Label exploredLabel;

    @FXML
    private Label pathLengthLabel;

    @FXML
    private Label timeLabel;
    
    @FXML
    private ComboBox<AlgorithmType> algorithmComboBox;

    private Grid grid;

    private boolean placingStart = false;
    private boolean placingEnd = false;

    private Cell startCell = null;
    private Cell endCell = null;

    @FXML
    public void initialize() {

        System.out.println("MainController chargé !");

        grid = new Grid();

        // Génération automatique des murs
        grid.generateRandomWalls();

        createGrid();
        
        algorithmComboBox.getItems().addAll(
            AlgorithmType.BFS,
            AlgorithmType.DFS,
            AlgorithmType.DIJKSTRA,
            AlgorithmType.ASTAR
        );
        
        // Bouton "Placer départ"
        startButton.setOnAction(event -> {
            placingStart = true;
            placingEnd = false;

            System.out.println("Choisissez une case pour le départ.");
        });

        // Bouton "Placer arrivée"
        endButton.setOnAction(event -> {
            placingEnd = true;
            placingStart = false;

            System.out.println("Choisissez une case pour l'arrivée.");
        });
        
        launchButton.setOnAction(event -> {
            runAlgorithm();
        });
        
        resetButton.setOnAction(event -> {
        resetGrid();
    });
    }

    private void createGrid() {

        double cellWidth = 650.0 / Grid.COLUMNS;
        double cellHeight = 480.0 / Grid.ROWS;

        for (int row = 0; row < Grid.ROWS; row++) {

            for (int column = 0; column < Grid.COLUMNS; column++) {

                Cell modelCell = grid.getCell(row, column);

                Region visualCell = new Region();

                visualCell.setPrefWidth(cellWidth);
                visualCell.setPrefHeight(cellHeight);

                updateCellStyle(visualCell, modelCell);

                // Clic sur une cellule
                visualCell.setOnMouseClicked(event -> {

                    handleCellClick(modelCell);

                });

                gridPane.add(visualCell, column, row);
            }
        }
    }

    private void handleCellClick(Cell cell) {

        // Placement du départ
        if (placingStart) {

            if (cell.isWall() || cell.isEnd()) {
                return;
            }

            // Supprimer l'ancien départ
            if (startCell != null) {
                startCell.setState(CellState.EMPTY);
            }

            // Créer le nouveau départ
            cell.setState(CellState.START);
            startCell = cell;

            placingStart = false;

            refreshGrid();

            System.out.println(
                "Départ : ligne "
                + cell.getRow()
                + ", colonne "
                + cell.getColumn()
            );

            return;
        }

        // Placement de l'arrivée
        if (placingEnd) {

            if (cell.isWall() || cell.isStart()) {
                return;
            }

            // Supprimer l'ancienne arrivée
            if (endCell != null) {
                endCell.setState(CellState.EMPTY);
            }

            // Créer la nouvelle arrivée
            cell.setState(CellState.END);
            endCell = cell;

            placingEnd = false;

            refreshGrid();

            System.out.println(
                "Arrivée : ligne "
                + cell.getRow()
                + ", colonne "
                + cell.getColumn()
            );
        }
    }
    
    @FXML
    private void runAlgorithm() {

        AlgorithmType selected = algorithmComboBox.getValue();
        PathFinderAlgorithm algorithm = null;

        if (selected == null) {
            System.out.println("Please select an algorithm.");
            return;
        }
        
        switch (selected) {

            case BFS:
                algorithm = new BFS();
                break;

            case DFS:
                algorithm = new DFS();
                break;

            case DIJKSTRA:
                algorithm = new Dijkstra();
                break;

            case ASTAR:
                algorithm = new AStar();
                break;
        }
        
        long startTime = System.nanoTime();

        PathResult result = algorithm.findPath(grid);

        long endTime = System.nanoTime();

        double executionTime = (endTime - startTime) / 1_000_000.0;

        int cellsExplored = result.getExploredCells().size();

        int pathLength = result.getPath().size();

        exploredLabel.setText(
                String.valueOf(cellsExplored)
        );

        pathLengthLabel.setText(
                String.valueOf(pathLength)
        );

        timeLabel.setText(
                String.format("%.3f ms", executionTime)
        );
    }

    private void refreshGrid() {

        gridPane.getChildren().clear();

        createGrid();
    }
    private void resetGrid() {

    grid = new Grid();

    grid.generateRandomWalls();

    startCell = null;
    endCell = null;

    placingStart = false;
    placingEnd = false;

    gridPane.getChildren().clear();

    createGrid();

    System.out.println("Grille réinitialisée !");
}

    private void updateCellStyle(Region visualCell, Cell cell) {

        switch (cell.getState()) {

            case START:

                visualCell.setStyle(
                    "-fx-background-color: green;" +
                    "-fx-border-color: lightgray;" +
                    "-fx-border-width: 0.5;"
                );

                break;

            case END:

                visualCell.setStyle(
                    "-fx-background-color: red;" +
                    "-fx-border-color: lightgray;" +
                    "-fx-border-width: 0.5;"
                );

                break;

            case WALL:

                visualCell.setStyle(
                    "-fx-background-color: darkgray;" +
                    "-fx-border-color: lightgray;" +
                    "-fx-border-width: 0.5;"
                );

                break;

            default:

                visualCell.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: lightgray;" +
                    "-fx-border-width: 0.5;"
                );

                break;
        }
    }
}

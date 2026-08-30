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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

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

    private Region[][] visualCells =
            new Region[Grid.ROWS][Grid.COLUMNS];

    private Timeline animation;

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

        // Dijkstra sélectionné par défaut
        algorithmComboBox.setValue(
                AlgorithmType.DIJKSTRA
        );

        // Bouton "Placer départ"
        startButton.setOnAction(event -> {

            placingStart = true;
            placingEnd = false;

            System.out.println(
                    "Choisissez une case pour le départ."
            );
        });

        // Bouton "Placer arrivée"
        endButton.setOnAction(event -> {

            placingEnd = true;
            placingStart = false;

            System.out.println(
                    "Choisissez une case pour l'arrivée."
            );
        });

        // Bouton "Lancer"
        launchButton.setOnAction(event -> {

            runAlgorithm();

        });

        // Bouton "Réinitialiser"
        resetButton.setOnAction(event -> {

            resetGrid();

        });
    }

    private void createGrid() {

        gridPane.getChildren().clear();

        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        gridPane.setHgap(0);
        gridPane.setVgap(0);

        // Création des 25 colonnes
        for (int column = 0;
                column < Grid.COLUMNS;
                column++) {

            ColumnConstraints columnConstraints =
                    new ColumnConstraints();

            columnConstraints.setPercentWidth(
                    100.0 / Grid.COLUMNS
            );

            columnConstraints.setHgrow(
                    Priority.ALWAYS
            );

            gridPane.getColumnConstraints().add(
                    columnConstraints
            );
        }

        // Création des 20 lignes
        for (int row = 0;
                row < Grid.ROWS;
                row++) {

            RowConstraints rowConstraints =
                    new RowConstraints();

            rowConstraints.setPercentHeight(
                    100.0 / Grid.ROWS
            );

            rowConstraints.setVgrow(
                    Priority.ALWAYS
            );

            gridPane.getRowConstraints().add(
                    rowConstraints
            );
        }

        // Création des cases
        for (int row = 0;
                row < Grid.ROWS;
                row++) {

            for (int column = 0;
                    column < Grid.COLUMNS;
                    column++) {

                Cell modelCell =
                        grid.getCell(
                                row,
                                column
                        );

                Region visualCell =
                        new Region();

                visualCell.setMinSize(
                        0,
                        0
                );

                visualCell.setMaxSize(
                        Double.MAX_VALUE,
                        Double.MAX_VALUE
                );

                GridPane.setHgrow(
                        visualCell,
                        Priority.ALWAYS
                );

                GridPane.setVgrow(
                        visualCell,
                        Priority.ALWAYS
                );

                updateCellStyle(
                        visualCell,
                        modelCell
                );

                visualCells[row][column] =
                        visualCell;

                visualCell.setOnMouseClicked(event -> {

                    handleCellClick(
                            modelCell
                    );

                });

                gridPane.add(
                        visualCell,
                        column,
                        row
                );
            }
        }
    }

    private void handleCellClick(Cell cell) {

        // Placement du départ
        if (placingStart) {

            if (cell.isWall()
                    || cell.isEnd()) {

                return;
            }

            // Supprimer l'ancien départ
            if (startCell != null) {

                startCell.setState(
                        CellState.EMPTY
                );
            }

            // Créer le nouveau départ
            cell.setState(
                    CellState.START
            );

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

            if (cell.isWall()
                    || cell.isStart()) {

                return;
            }

            // Supprimer l'ancienne arrivée
            if (endCell != null) {

                endCell.setState(
                        CellState.EMPTY
                );
            }

            // Créer la nouvelle arrivée
            cell.setState(
                    CellState.END
            );

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

        AlgorithmType selected =
                algorithmComboBox.getValue();

        PathFinderAlgorithm algorithm = null;

        if (selected == null) {

            System.out.println(
                    "Veuillez sélectionner un algorithme."
            );

            return;
        }

        if (grid.getStart() == null
                || grid.getEnd() == null) {

            System.out.println(
                    "Veuillez placer un départ et une arrivée."
            );

            return;
        }

        // Arrêter une animation précédente
        if (animation != null) {

            animation.stop();

        }

        // Effacer l'ancien résultat
        clearAlgorithmStates();

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

        long startTime =
                System.nanoTime();

        PathResult result =
                algorithm.findPath(grid);

        long endTime =
                System.nanoTime();

        double executionTime =
                (endTime - startTime)
                / 1_000_000.0;

        int cellsExplored =
                result.getExploredCells().size();

        int pathLength =
                result.getPath().size();

        exploredLabel.setText(
                String.valueOf(
                        cellsExplored
                )
        );

        pathLengthLabel.setText(
                String.valueOf(
                        pathLength
                )
        );

        timeLabel.setText(
                String.format(
                        "%.3f ms",
                        executionTime
                )
        );

        // Lancer l'animation
        animateResult(
                result
        );
    }

    private void animateResult(
            PathResult result) {

        animation =
                new Timeline();

        int step = 0;

        // Affichage progressif des cases explorées
        for (Cell cell :
                result.getExploredCells()) {

            if (!cell.isStart()
                    && !cell.isEnd()
                    && !cell.isWall()) {

                final Cell currentCell =
                        cell;

                KeyFrame frame =
                        new KeyFrame(

                    Duration.millis(
                            step * 12
                    ),

                    event -> {

                        currentCell.setState(
                                CellState.EXPLORED
                        );

                        updateVisualCell(
                                currentCell
                        );
                    }
                );

                animation
                        .getKeyFrames()
                        .add(frame);

                step++;
            }
        }

        // Petite pause avant l'affichage du chemin
        step += 8;

        // Affichage progressif du chemin
        for (Cell cell :
                result.getPath()) {

            if (!cell.isStart()
                    && !cell.isEnd()
                    && !cell.isWall()) {

                final Cell currentCell =
                        cell;

                KeyFrame frame =
                        new KeyFrame(

                    Duration.millis(
                            step * 12
                    ),

                    event -> {

                        currentCell.setState(
                                CellState.PATH
                        );

                        updateVisualCell(
                                currentCell
                        );
                    }
                );

                animation
                        .getKeyFrames()
                        .add(frame);

                step += 3;
            }
        }

        launchButton.setDisable(
                true
        );

        animation.setOnFinished(event -> {

            launchButton.setDisable(
                    false
            );

        });

        animation.play();
    }

    private void updateVisualCell(
            Cell cell) {

        Region visualCell =
                visualCells
                [cell.getRow()]
                [cell.getColumn()];

        if (visualCell != null) {

            updateCellStyle(
                    visualCell,
                    cell
            );
        }
    }

    private void clearAlgorithmStates() {

        for (int row = 0;
                row < Grid.ROWS;
                row++) {

            for (int column = 0;
                    column < Grid.COLUMNS;
                    column++) {

                Cell cell =
                        grid.getCell(
                                row,
                                column
                        );

                if (cell.getState()
                        == CellState.EXPLORED
                        || cell.getState()
                        == CellState.PATH) {

                    cell.setState(
                            CellState.EMPTY
                    );
                }
            }
        }

        refreshGrid();
    }

    private void refreshGrid() {

        for (int row = 0;
                row < Grid.ROWS;
                row++) {

            for (int column = 0;
                    column < Grid.COLUMNS;
                    column++) {

                Cell cell =
                        grid.getCell(
                                row,
                                column
                        );

                Region visualCell =
                        visualCells
                        [row]
                        [column];

                if (visualCell != null) {

                    updateCellStyle(
                            visualCell,
                            cell
                    );
                }
            }
        }
    }

    private void resetGrid() {

        if (animation != null) {

            animation.stop();

        }

        grid = new Grid();

        grid.generateRandomWalls();

        startCell = null;
        endCell = null;

        placingStart = false;
        placingEnd = false;

        exploredLabel.setText(
                "0"
        );

        pathLengthLabel.setText(
                "0"
        );

        timeLabel.setText(
                "0 ms"
        );

        visualCells =
                new Region
                [Grid.ROWS]
                [Grid.COLUMNS];

        createGrid();

        launchButton.setDisable(
                false
        );

        System.out.println(
                "Grille réinitialisée !"
        );
    }

    private void updateCellStyle(
            Region visualCell,
            Cell cell) {

        switch (cell.getState()) {

            case START:

                visualCell.setStyle(
                    "-fx-background-color: green;"
                    + "-fx-border-color: lightgray;"
                    + "-fx-border-width: 0.5;"
                );

                break;

            case END:

                visualCell.setStyle(
                    "-fx-background-color: red;"
                    + "-fx-border-color: lightgray;"
                    + "-fx-border-width: 0.5;"
                );

                break;

            case WALL:

                visualCell.setStyle(
                    "-fx-background-color: darkgray;"
                    + "-fx-border-color: lightgray;"
                    + "-fx-border-width: 0.5;"
                );

                break;

            case EXPLORED:

                visualCell.setStyle(
                    "-fx-background-color: lightblue;"
                    + "-fx-border-color: lightgray;"
                    + "-fx-border-width: 0.5;"
                );

                break;

            case PATH:

                visualCell.setStyle(
                    "-fx-background-color: gold;"
                    + "-fx-border-color: lightgray;"
                    + "-fx-border-width: 0.5;"
                );

                break;

            default:

                visualCell.setStyle(
                    "-fx-background-color: white;"
                    + "-fx-border-color: lightgray;"
                    + "-fx-border-width: 0.5;"
                );

                break;
        }
    }
}
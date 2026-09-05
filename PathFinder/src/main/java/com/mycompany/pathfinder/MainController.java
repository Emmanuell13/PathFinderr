/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 */
package com.mycompany.pathfinder;

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

import com.mycompany.pathfinder.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.application.Platform;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
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

    // =========================================================
    // ELEMENTS FXML
    // =========================================================

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
    private Button compareButton;

    @FXML
    private Label exploredLabel;

    @FXML
    private Label pathLengthLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private ComboBox<AlgorithmType> algorithmComboBox;


    // =========================================================
    // GRILLE
    // =========================================================

    private Grid grid;

    private boolean placingStart = false;
    private boolean placingEnd = false;

    private Cell startCell = null;
    private Cell endCell = null;

    private Region[][] visualCells =
            new Region[Grid.ROWS][Grid.COLUMNS];


    // =========================================================
    // ANIMATION
    // =========================================================

    private Timeline animation;


    // =========================================================
    // EXECUTOR
    // =========================================================

    /*
     * 4 threads permettent aux 4 algorithmes
     * de travailler en parallèle.
     */
    private final ExecutorService executorService =
            Executors.newFixedThreadPool(4);


    /*
     * Sert à déterminer le vrai ordre d'arrivée.
     */
    private final AtomicInteger arrivalCounter =
            new AtomicInteger(0);


    // =========================================================
    // COULEURS DES CHEMINS UNIQUEMENT
    // =========================================================

    private static final String BFS_COLOR =
            "#2196F3";

    private static final String DFS_COLOR =
            "#4CAF50";

    private static final String DIJKSTRA_COLOR =
            "#9C27B0";

    private static final String ASTAR_COLOR =
            "#FF9800";


    // =========================================================
    // RESULTAT ALGORITHME
    // =========================================================

    private static class AlgorithmResult {

        private final AlgorithmType algorithm;
        private final PathResult result;
        private final double executionTime;
        private final long completionTime;
        private final int arrivalOrder;


        public AlgorithmResult(
                AlgorithmType algorithm,
                PathResult result,
                double executionTime,
                long completionTime,
                int arrivalOrder) {

            this.algorithm = algorithm;
            this.result = result;
            this.executionTime = executionTime;
            this.completionTime = completionTime;
            this.arrivalOrder = arrivalOrder;
        }


        public AlgorithmType getAlgorithm() {
            return algorithm;
        }


        public PathResult getResult() {
            return result;
        }


        public double getExecutionTime() {
            return executionTime;
        }


        public long getCompletionTime() {
            return completionTime;
        }


        public int getArrivalOrder() {
            return arrivalOrder;
        }
    }


    // =========================================================
    // INITIALIZE
    // =========================================================

    @FXML
    public void initialize() {

        System.out.println(
                "MainController chargé !"
        );


        grid = new Grid();


        // Génération automatique des murs
        grid.generateRandomWalls();


        createGrid();


        // =====================================================
        // COMBOBOX
        // =====================================================

        algorithmComboBox.getItems().clear();

        algorithmComboBox.getItems().addAll(
                AlgorithmType.BFS,
                AlgorithmType.DFS,
                AlgorithmType.DIJKSTRA,
                AlgorithmType.ASTAR
        );


        algorithmComboBox.setValue(
                AlgorithmType.DIJKSTRA
        );


        // =====================================================
        // BOUTON DEPART
        // =====================================================

        startButton.setOnAction(event -> {

            placingStart = true;
            placingEnd = false;

            System.out.println(
                    "Choisissez une case pour le départ."
            );
        });


        // =====================================================
        // BOUTON ARRIVEE
        // =====================================================

        endButton.setOnAction(event -> {

            placingEnd = true;
            placingStart = false;

            System.out.println(
                    "Choisissez une case pour l'arrivée."
            );
        });


        // =====================================================
        // BOUTON LANCER
        // =====================================================

        launchButton.setOnAction(event -> {

            runAlgorithm();

        });


        // =====================================================
        // BOUTON RESET
        // =====================================================

        resetButton.setOnAction(event -> {

            resetGrid();

        });


        // =====================================================
        // BOUTON COMPARER
        // =====================================================

        compareButton.setOnAction(event -> {

            runAllAlgorithms();

        });
    }


    // =========================================================
    // CREATION DE LA GRILLE
    // =========================================================

    private void createGrid() {

        gridPane.getChildren().clear();

        gridPane.getColumnConstraints().clear();

        gridPane.getRowConstraints().clear();

        gridPane.setHgap(0);

        gridPane.setVgap(0);


        // =====================================================
        // COLONNES
        // =====================================================

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


        // =====================================================
        // LIGNES
        // =====================================================

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


        // =====================================================
        // CASES
        // =====================================================

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


    // =========================================================
    // CLIC SUR UNE CASE
    // =========================================================

    private void handleCellClick(Cell cell) {

        // =====================================================
        // DEPART
        // =====================================================

        if (placingStart) {

            if (cell.isWall()
                    || cell.isEnd()) {

                return;
            }


            if (startCell != null) {

                startCell.setState(
                        CellState.EMPTY
                );
            }


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


        // =====================================================
        // ARRIVEE
        // =====================================================

        if (placingEnd) {

            if (cell.isWall()
                    || cell.isStart()) {

                return;
            }


            if (endCell != null) {

                endCell.setState(
                        CellState.EMPTY
                );
            }


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


    // =========================================================
    // LANCER UN SEUL ALGORITHME
    // =========================================================

    @FXML
    private void runAlgorithm() {

        AlgorithmType selected =
                algorithmComboBox.getValue();


        if (selected == null) {

            return;
        }


        if (grid.getStart() == null
                || grid.getEnd() == null) {

            System.out.println(
                    "Veuillez placer un départ et une arrivée."
            );

            return;
        }


        if (animation != null) {

            animation.stop();
        }


        clearAlgorithmStates();


        PathFinderAlgorithm algorithm =
                createAlgorithm(selected);


        if (algorithm == null) {

            return;
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


        exploredLabel.setText(
                String.valueOf(
                        result.getExploredCells().size()
                )
        );


        pathLengthLabel.setText(
                String.valueOf(
                        result.getPath().size()
                )
        );


        timeLabel.setText(
                String.format(
                        "%.3f ms",
                        executionTime
                )
        );


        saveResultToDatabase(
                selected,
                result,
                executionTime
        );


        animateResult(
                result
        );
    }


    // =========================================================
    // CREER ALGORITHME
    // =========================================================

    private PathFinderAlgorithm createAlgorithm(
            AlgorithmType type) {

        switch (type) {

            case BFS:
                return new BFS();

            case DFS:
                return new DFS();

            case DIJKSTRA:
                return new Dijkstra();

            case ASTAR:
                return new AStar();

            default:
                return null;
        }
    }


    // =========================================================
    // LANCER LES 4 ALGORITHMES
    // =========================================================

    private void runAllAlgorithms() {

        if (grid.getStart() == null
                || grid.getEnd() == null) {

            System.out.println(
                    "Veuillez placer un départ et une arrivée."
            );

            return;
        }


        if (animation != null) {

            animation.stop();
        }


        clearAlgorithmStates();


        // =====================================================
        // DESACTIVER LES BOUTONS
        // =====================================================

        compareButton.setDisable(true);
        launchButton.setDisable(true);
        startButton.setDisable(true);
        endButton.setDisable(true);
        resetButton.setDisable(true);


        System.out.println();
        System.out.println(
                "======================================"
        );
        System.out.println(
                "       COURSE DES 4 ALGORITHMES"
        );
        System.out.println(
                "======================================"
        );


        // =====================================================
        // COPIES IDENTIQUES
        // =====================================================

        Grid bfsGrid =
                copyGrid(grid);

        Grid dfsGrid =
                copyGrid(grid);

        Grid dijkstraGrid =
                copyGrid(grid);

        Grid astarGrid =
                copyGrid(grid);


        // =====================================================
        // BARRIERE DE DEPART
        // =====================================================

        /*
         * Les 4 algorithmes doivent arriver ici.
         *
         * Aucun ne commence avant que les 4
         * soient prêts.
         */
        CyclicBarrier startBarrier =
                new CyclicBarrier(4);


        arrivalCounter.set(0);


        // =====================================================
        // BFS
        // =====================================================

        CompletableFuture<AlgorithmResult> bfsFuture =
                CompletableFuture.supplyAsync(
                        () -> executeAlgorithm(
                                AlgorithmType.BFS,
                                new BFS(),
                                bfsGrid,
                                startBarrier
                        ),
                        executorService
                );


        // =====================================================
        // DFS
        // =====================================================

        CompletableFuture<AlgorithmResult> dfsFuture =
                CompletableFuture.supplyAsync(
                        () -> executeAlgorithm(
                                AlgorithmType.DFS,
                                new DFS(),
                                dfsGrid,
                                startBarrier
                        ),
                        executorService
                );


        // =====================================================
        // DIJKSTRA
        // =====================================================

        CompletableFuture<AlgorithmResult> dijkstraFuture =
                CompletableFuture.supplyAsync(
                        () -> executeAlgorithm(
                                AlgorithmType.DIJKSTRA,
                                new Dijkstra(),
                                dijkstraGrid,
                                startBarrier
                        ),
                        executorService
                );


        // =====================================================
        // A*
        // =====================================================

        CompletableFuture<AlgorithmResult> astarFuture =
                CompletableFuture.supplyAsync(
                        () -> executeAlgorithm(
                                AlgorithmType.ASTAR,
                                new AStar(),
                                astarGrid,
                                startBarrier
                        ),
                        executorService
                );


        // =====================================================
        // ATTENDRE LES 4
        // =====================================================

        CompletableFuture
                .allOf(
                        bfsFuture,
                        dfsFuture,
                        dijkstraFuture,
                        astarFuture
                )
                .thenRun(() -> {

                    try {

                        List<AlgorithmResult> results =
                                new ArrayList<>();


                        results.add(
                                bfsFuture.join()
                        );


                        results.add(
                                dfsFuture.join()
                        );


                        results.add(
                                dijkstraFuture.join()
                        );


                        results.add(
                                astarFuture.join()
                        );


                        // =================================================
                        // TRI PAR ORDRE REEL D'ARRIVEE
                        // =================================================

                        results.sort(
                                Comparator.comparingInt(
                                        AlgorithmResult::getArrivalOrder
                                )
                        );


                        Platform.runLater(() -> {

                            printArrivalOrder(
                                    results
                            );


                            saveComparisonResults(
                                    results
                            );


                            /*
                             * UNE SEULE ANIMATION
                             *
                             * Les 4 algorithmes sont affichés
                             * ensemble.
                             */
                            animateAllAlgorithms(
                                    results
                            );
                        });


                    } catch (Exception e) {

                        e.printStackTrace();


                        Platform.runLater(() -> {

                            enableButtons();


                            showError(
                                    "Erreur pendant la comparaison",
                                    e.getMessage()
                            );
                        });
                    }
                });
    }


    // =========================================================
    // EXECUTER ALGORITHME AVEC BARRIERE
    // =========================================================

    private AlgorithmResult executeAlgorithm(
            AlgorithmType algorithmType,
            PathFinderAlgorithm algorithm,
            Grid algorithmGrid,
            CyclicBarrier startBarrier) {

        try {

            /*
             * ATTENDRE QUE LES 4 SOIENT PRETS
             */
            startBarrier.await();


            /*
             * DEPART COMMUN
             */
            long startTime =
                    System.nanoTime();


            /*
             * EXECUTION
             */
            PathResult result =
                    algorithm.findPath(
                            algorithmGrid
                    );


            /*
             * FIN
             */
            long endTime =
                    System.nanoTime();


            double executionTime =
                    (endTime - startTime)
                    / 1_000_000.0;


            /*
             * ORDRE REEL D'ARRIVEE
             */
            int arrivalOrder =
                    arrivalCounter.incrementAndGet();


            long completionTime =
                    System.nanoTime();


            return new AlgorithmResult(
                    algorithmType,
                    result,
                    executionTime,
                    completionTime,
                    arrivalOrder
            );


        } catch (Exception e) {

            throw new RuntimeException(
                    "Erreur avec "
                    + algorithmType,
                    e
            );
        }
    }


    // =========================================================
    // COPIER LA GRILLE
    // =========================================================

    private Grid copyGrid(Grid original) {

        Grid copy =
                new Grid();


        for (int row = 0;
                row < Grid.ROWS;
                row++) {

            for (int column = 0;
                    column < Grid.COLUMNS;
                    column++) {


                Cell originalCell =
                        original.getCell(
                                row,
                                column
                        );


                Cell copyCell =
                        copy.getCell(
                                row,
                                column
                        );


                copyCell.setState(
                        originalCell.getState()
                );
            }
        }


        return copy;
    }


    // =========================================================
    // ANIMATION DES 4 ALGORITHMES
    // =========================================================

    private void animateAllAlgorithms(
            List<AlgorithmResult> results) {

        animation =
                new Timeline();


        /*
         * Les explorations restent neutres.
         */
        Map<AlgorithmType, List<Cell>>
                exploredMap =
                new HashMap<>();


        /*
         * Les chemins sont séparés par algorithme.
         */
        Map<AlgorithmType, List<Cell>>
                pathMap =
                new HashMap<>();


        int maxExplored = 0;

        int maxPath = 0;


        // =====================================================
        // PREPARER LES RESULTATS
        // =====================================================

        for (AlgorithmResult algorithmResult :
                results) {


            AlgorithmType algorithm =
                    algorithmResult.getAlgorithm();


            PathResult result =
                    algorithmResult.getResult();


            List<Cell> explored =
                    result.getExploredCells();


            List<Cell> path =
                    result.getPath();


            exploredMap.put(
                    algorithm,
                    explored
            );


            pathMap.put(
                    algorithm,
                    path
            );


            maxExplored =
                    Math.max(
                            maxExplored,
                            explored.size()
                    );


            maxPath =
                    Math.max(
                            maxPath,
                            path.size()
                    );
        }


        // =====================================================
        // EXPLORATION
        // =====================================================

        /*
         * Une Timeline commune.
         *
         * A chaque instant, on avance
         * dans les 4 listes.
         *
         * MAIS toutes les explorations
         * ont la même couleur neutre.
         */
        for (int step = 0;
                step < maxExplored;
                step++) {


            final int currentStep =
                    step;


            KeyFrame frame =
                    new KeyFrame(

                            Duration.millis(
                                    currentStep * 12
                            ),

                            event -> {


                                for (AlgorithmType algorithm :
                                        AlgorithmType.values()) {


                                    List<Cell> explored =
                                            exploredMap.get(
                                                    algorithm
                                            );


                                    if (explored == null) {

                                        continue;
                                    }


                                    if (currentStep
                                            >= explored.size()) {

                                        continue;
                                    }


                                    Cell cell =
                                            explored.get(
                                                    currentStep
                                            );


                                    colorExploredCell(
                                            cell
                                    );
                                }
                            }
                    );


            animation
                    .getKeyFrames()
                    .add(frame);
        }


        // =====================================================
        // CHEMINS
        // =====================================================

        /*
         * Les 4 chemins commencent après l'exploration.
         *
         * Ils progressent simultanément.
         *
         * C'est uniquement ICI que les couleurs
         * des algorithmes sont utilisées.
         */
        int pathStart =
                maxExplored * 12 + 200;


        for (int step = 0;
                step < maxPath;
                step++) {


            final int currentStep =
                    step;


            KeyFrame frame =
                    new KeyFrame(

                            Duration.millis(
                                    pathStart
                                    + currentStep * 80
                            ),

                            event -> {


                                for (AlgorithmType algorithm :
                                        AlgorithmType.values()) {


                                    List<Cell> path =
                                            pathMap.get(
                                                    algorithm
                                            );


                                    if (path == null) {

                                        continue;
                                    }


                                    if (currentStep
                                            >= path.size()) {

                                        continue;
                                    }


                                    Cell cell =
                                            path.get(
                                                    currentStep
                                            );


                                    colorPathCell(
                                            cell,
                                            algorithm
                                    );
                                }
                            }
                    );


            animation
                    .getKeyFrames()
                    .add(frame);
        }


        // =====================================================
        // FIN
        // =====================================================

        animation.setOnFinished(event -> {

            enableButtons();


            displayArrivalOrder(
                    results
            );


            System.out.println(
                    "Animation terminée."
            );
        });


        animation.play();
    }


    // =========================================================
    // EXPLORATION NEUTRE
    // =========================================================

    private void colorExploredCell(
            Cell cell) {

        if (cell == null) {

            return;
        }


        if (cell.isStart()
                || cell.isEnd()
                || cell.isWall()) {

            return;
        }


        Region visualCell =
                visualCells
                        [cell.getRow()]
                        [cell.getColumn()];


        if (visualCell == null) {

            return;
        }


        /*
         * IMPORTANT :
         *
         * Tous les algorithmes ont exactement
         * la même couleur d'exploration.
         *
         * Les couleurs différentes sont réservées
         * aux chemins.
         */
        visualCell.setStyle(

                "-fx-background-color: lightblue;"
                + "-fx-border-color: lightgray;"
                + "-fx-border-width: 0.5;"
        );
    }


    // =========================================================
    // COLORER CHEMIN
    // =========================================================

    private void colorPathCell(
            Cell cell,
            AlgorithmType algorithm) {

        if (cell == null) {

            return;
        }


        if (cell.isStart()
                || cell.isEnd()
                || cell.isWall()) {

            return;
        }


        Region visualCell =
                visualCells
                        [cell.getRow()]
                        [cell.getColumn()];


        if (visualCell == null) {

            return;
        }


        /*
         * Récupérer les algorithmes qui ont déjà
         * utilisé cette case dans leur chemin.
         */
        Set<AlgorithmType> algorithms =
                getPathAlgorithms(
                        visualCell
                );


        algorithms.add(
                algorithm
        );


        applyPathStyle(
                visualCell,
                algorithms
        );
    }


    // =========================================================
    // DETECTER LES CHEMINS DEJA PRESENTS
    // =========================================================

    private Set<AlgorithmType> getPathAlgorithms(
            Region visualCell) {

        Set<AlgorithmType> algorithms =
                EnumSet.noneOf(
                        AlgorithmType.class
                );


        String style =
                visualCell.getStyle();


        if (style == null) {

            return algorithms;
        }


        if (style.contains(BFS_COLOR)) {

            algorithms.add(
                    AlgorithmType.BFS
            );
        }


        if (style.contains(DFS_COLOR)) {

            algorithms.add(
                    AlgorithmType.DFS
            );
        }


        if (style.contains(DIJKSTRA_COLOR)) {

            algorithms.add(
                    AlgorithmType.DIJKSTRA
            );
        }


        if (style.contains(ASTAR_COLOR)) {

            algorithms.add(
                    AlgorithmType.ASTAR
            );
        }


        return algorithms;
    }


    // =========================================================
    // STYLE CHEMIN
    // =========================================================

    private void applyPathStyle(
            Region visualCell,
            Set<AlgorithmType> algorithms) {

        if (algorithms.isEmpty()) {

            return;
        }


        /*
         * Un seul algorithme :
         * couleur normale.
         */
        if (algorithms.size() == 1) {

            AlgorithmType algorithm =
                    algorithms.iterator().next();


            visualCell.setStyle(

                    "-fx-background-color: "
                    + getAlgorithmColor(
                            algorithm
                    )
                    + ";"
                    + "-fx-border-color: black;"
                    + "-fx-border-width: 1;"
            );


            return;
        }


        /*
         * Plusieurs chemins se superposent.
         *
         * On divise visuellement la case.
         */
        visualCell.setStyle(

                "-fx-background-color: "
                + createGradient(
                        algorithms
                )
                + ";"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;"
        );
    }


    // =========================================================
    // GRADIENT CHEMINS SUPERPOSES
    // =========================================================

    private String createGradient(
            Set<AlgorithmType> algorithms) {

        List<String> colors =
                new ArrayList<>();


        /*
         * On garde toujours le même ordre :
         *
         * BFS
         * DFS
         * Dijkstra
         * A*
         */
        for (AlgorithmType algorithm :
                AlgorithmType.values()) {


            if (algorithms.contains(
                    algorithm
            )) {


                colors.add(
                        getAlgorithmColor(
                                algorithm
                        )
                );
            }
        }


        if (colors.size() == 2) {

            return "linear-gradient("
                    + "to right, "
                    + colors.get(0)
                    + " 0%, "
                    + colors.get(0)
                    + " 50%, "
                    + colors.get(1)
                    + " 50%, "
                    + colors.get(1)
                    + " 100%"
                    + ")";
        }


        if (colors.size() == 3) {

            return "linear-gradient("
                    + "to right, "
                    + colors.get(0)
                    + " 0%, "
                    + colors.get(0)
                    + " 33%, "
                    + colors.get(1)
                    + " 33%, "
                    + colors.get(1)
                    + " 66%, "
                    + colors.get(2)
                    + " 66%, "
                    + colors.get(2)
                    + " 100%"
                    + ")";
        }


        return "linear-gradient("
                + "to right, "
                + colors.get(0)
                + " 0%, "
                + colors.get(0)
                + " 25%, "
                + colors.get(1)
                + " 25%, "
                + colors.get(1)
                + " 50%, "
                + colors.get(2)
                + " 50%, "
                + colors.get(2)
                + " 75%, "
                + colors.get(3)
                + " 75%, "
                + colors.get(3)
                + " 100%"
                + ")";
    }


    // =========================================================
    // COULEUR ALGORITHME
    // =========================================================

    private String getAlgorithmColor(
            AlgorithmType algorithm) {

        switch (algorithm) {

            case BFS:

                return BFS_COLOR;


            case DFS:

                return DFS_COLOR;


            case DIJKSTRA:

                return DIJKSTRA_COLOR;


            case ASTAR:

                return ASTAR_COLOR;


            default:

                return "#FFFFFF";
        }
    }


    // =========================================================
    // ANIMATION ALGORITHME UNIQUE
    // =========================================================

    private void animateResult(
            PathResult result) {

        animation =
                new Timeline();


        int step = 0;


        // =====================================================
        // EXPLORATION
        // =====================================================

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


        step += 8;


        // =====================================================
        // CHEMIN
        // =====================================================

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


        launchButton.setDisable(true);


        animation.setOnFinished(event -> {

            launchButton.setDisable(false);

        });


        animation.play();
    }


    // =========================================================
    // AFFICHER ORDRE ARRIVEE
    // =========================================================

    private void displayArrivalOrder(
            List<AlgorithmResult> results) {

        if (results.isEmpty()) {

            return;
        }


        StringBuilder message =
                new StringBuilder();


        message.append(
                "ORDRE D'ARRIVÉE\n\n"
        );


        for (int i = 0;
                i < results.size();
                i++) {


            AlgorithmResult current =
                    results.get(i);


            String position;


            if (i == 0) {

                position = "🥇 1er";

            } else if (i == 1) {

                position = "🥈 2e";

            } else if (i == 2) {

                position = "🥉 3e";

            } else {

                position = "4e";
            }


            message.append(
                    position
            );


            message.append(
                    " : "
            );


            message.append(
                    getAlgorithmName(
                            current.getAlgorithm()
                    )
            );


            message.append(
                    "\nTemps : "
            );


            message.append(
                    String.format(
                            "%.3f ms",
                            current.getExecutionTime()
                    )
            );


            message.append(
                    "\nCases explorées : "
            );


            message.append(
                    current
                            .getResult()
                            .getExploredCells()
                            .size()
            );


            message.append(
                    "\nChemin : "
            );


            message.append(
                    current
                            .getResult()
                            .getPath()
                            .size()
            );


            message.append(
                    "\n\n"
            );
        }


        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );


        alert.setTitle(
                "Comparaison des algorithmes"
        );


        alert.setHeaderText(
                "🏁 Ordre d'arrivée"
        );


        alert.setContentText(
                message.toString()
        );


        alert.show();
    }


    // =========================================================
    // CONSOLE ORDRE ARRIVEE
    // =========================================================

    private void printArrivalOrder(
            List<AlgorithmResult> results) {

        System.out.println();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "       ORDRE REEL D'ARRIVEE"
        );

        System.out.println(
                "======================================"
        );


        for (int i = 0;
                i < results.size();
                i++) {


            AlgorithmResult current =
                    results.get(i);


            System.out.println(
                    (i + 1)
                    + "e : "
                    + getAlgorithmName(
                            current.getAlgorithm()
                    )
                    + " | Temps : "
                    + String.format(
                            "%.3f ms",
                            current.getExecutionTime()
                    )
                    + " | Explorees : "
                    + current
                            .getResult()
                            .getExploredCells()
                            .size()
                    + " | Chemin : "
                    + current
                            .getResult()
                            .getPath()
                            .size()
            );
        }


        System.out.println(
                "======================================"
        );
    }


    // =========================================================
    // NOM ALGORITHME
    // =========================================================

    private String getAlgorithmName(
            AlgorithmType algorithm) {

        switch (algorithm) {

            case BFS:
                return "BFS";

            case DFS:
                return "DFS";

            case DIJKSTRA:
                return "Dijkstra";

            case ASTAR:
                return "A*";

            default:
                return algorithm.name();
        }
    }


    // =========================================================
    // SAUVEGARDE COMPARAISON
    // =========================================================

    private void saveComparisonResults(
            List<AlgorithmResult> results) {

        for (AlgorithmResult result :
                results) {


            saveResultToDatabase(
                    result.getAlgorithm(),
                    result.getResult(),
                    result.getExecutionTime()
            );
        }
    }


    // =========================================================
    // SAUVEGARDE DATABASE
    // =========================================================

    private void saveResultToDatabase(
            AlgorithmType selected,
            PathResult result,
            double executionTime) {


        String insertGrid =
                "INSERT INTO grid "
                + "(rows_grid, cols_grid) "
                + "VALUES (?, ?) "
                + "RETURNING grid_id";


        String insertGridCell =
                "INSERT INTO grid_cell "
                + "(grid_id, row_index, col_index, cell_type) "
                + "VALUES (?, ?, ?, ?)";


        String insertAlgoRun =
                "INSERT INTO algo_run "
                + "(grid_id, algorithm, execution_time, "
                + "cells_explored, path_length) "
                + "VALUES (?, ?, ?, ?, ?) "
                + "RETURNING run_id";


        String insertPathCell =
                "INSERT INTO path_cell "
                + "(run_id, row_index, col_index, cell_state) "
                + "VALUES (?, ?, ?, ?)";


        try (Connection connection =
                DatabaseConnection.getConnection()) {


            connection.setAutoCommit(false);


            try {


                // =================================================
                // GRILLE
                // =================================================

                int gridId;


                try (PreparedStatement statement =
                        connection.prepareStatement(
                                insertGrid
                        )) {


                    statement.setInt(
                            1,
                            Grid.ROWS
                    );


                    statement.setInt(
                            2,
                            Grid.COLUMNS
                    );


                    try (ResultSet resultSet =
                            statement.executeQuery()) {


                        if (!resultSet.next()) {

                            throw new SQLException(
                                    "Impossible de récupérer grid_id."
                            );
                        }


                        gridId =
                                resultSet.getInt(
                                        "grid_id"
                                );
                    }
                }


                // =================================================
                // WALL / START / END
                // =================================================

                try (PreparedStatement statement =
                        connection.prepareStatement(
                                insertGridCell
                        )) {


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


                            String cellType =
                                    null;


                            if (cell.getState()
                                    == CellState.WALL) {


                                cellType =
                                        "WALL";


                            } else if (
                                    cell.getState()
                                    == CellState.START) {


                                cellType =
                                        "START";


                            } else if (
                                    cell.getState()
                                    == CellState.END) {


                                cellType =
                                        "END";
                            }


                            if (cellType != null) {


                                statement.setInt(
                                        1,
                                        gridId
                                );


                                statement.setInt(
                                        2,
                                        row
                                );


                                statement.setInt(
                                        3,
                                        column
                                );


                                statement.setObject(
                                        4,
                                        cellType,
                                        Types.OTHER
                                );


                                statement.addBatch();
                            }
                        }
                    }


                    statement.executeBatch();
                }


                // =================================================
                // EXECUTION
                // =================================================

                int runId;


                try (PreparedStatement statement =
                        connection.prepareStatement(
                                insertAlgoRun
                        )) {


                    statement.setInt(
                            1,
                            gridId
                    );


                    statement.setObject(
                            2,
                            selected.name(),
                            Types.OTHER
                    );


                    statement.setDouble(
                            3,
                            executionTime
                    );


                    statement.setInt(
                            4,
                            result
                                    .getExploredCells()
                                    .size()
                    );


                    statement.setInt(
                            5,
                            result
                                    .getPath()
                                    .size()
                    );


                    try (ResultSet resultSet =
                            statement.executeQuery()) {


                        if (!resultSet.next()) {

                            throw new SQLException(
                                    "Impossible de récupérer run_id."
                            );
                        }


                        runId =
                                resultSet.getInt(
                                        "run_id"
                                );
                    }
                }


                // =================================================
                // EXPLORED + PATH
                // =================================================

                try (PreparedStatement statement =
                        connection.prepareStatement(
                                insertPathCell
                        )) {


                    for (Cell cell :
                            result.getExploredCells()) {


                        statement.setInt(
                                1,
                                runId
                        );


                        statement.setInt(
                                2,
                                cell.getRow()
                        );


                        statement.setInt(
                                3,
                                cell.getColumn()
                        );


                        statement.setObject(
                                4,
                                "EXPLORED",
                                Types.OTHER
                        );


                        statement.addBatch();
                    }


                    for (Cell cell :
                            result.getPath()) {


                        statement.setInt(
                                1,
                                runId
                        );


                        statement.setInt(
                                2,
                                cell.getRow()
                        );


                        statement.setInt(
                                3,
                                cell.getColumn()
                        );


                        statement.setObject(
                                4,
                                "PATH",
                                Types.OTHER
                        );


                        statement.addBatch();
                    }


                    statement.executeBatch();
                }


                // =================================================
                // COMMIT
                // =================================================

                connection.commit();


                System.out.println(
                        "Résultat enregistré dans PostgreSQL."
                );


            } catch (Exception e) {

                connection.rollback();

                throw e;
            }


        } catch (Exception e) {

            System.err.println(
                    "Erreur DB : "
                    + e.getMessage()
            );


            e.printStackTrace();
        }
    }


    // =========================================================
    // UPDATE VISUEL
    // =========================================================

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


    // =========================================================
    // SUPPRIMER RESULTATS
    // =========================================================

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


    // =========================================================
    // REFRESH
    // =========================================================

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


    // =========================================================
    // RESET
    // =========================================================

    private void resetGrid() {

        if (animation != null) {

            animation.stop();
        }


        grid =
                new Grid();


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


        enableButtons();


        System.out.println(
                "Grille réinitialisée !"
        );
    }


    // =========================================================
    // REACTIVER BOUTONS
    // =========================================================

    private void enableButtons() {

        compareButton.setDisable(false);

        launchButton.setDisable(false);

        startButton.setDisable(false);

        endButton.setDisable(false);

        resetButton.setDisable(false);
    }


    // =========================================================
    // MESSAGE ERREUR
    // =========================================================

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );


        alert.setTitle(
                title
        );


        alert.setHeaderText(
                null
        );


        alert.setContentText(
                message
        );


        alert.showAndWait();
    }


    // =========================================================
    // STYLE DES CASES
    // =========================================================

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

                /*
                 * Exploration neutre.
                 */
                visualCell.setStyle(

                        "-fx-background-color: lightblue;"
                        + "-fx-border-color: lightgray;"
                        + "-fx-border-width: 0.5;"
                );

                break;


            case PATH:

                /*
                 * Pour un seul algorithme.
                 */
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
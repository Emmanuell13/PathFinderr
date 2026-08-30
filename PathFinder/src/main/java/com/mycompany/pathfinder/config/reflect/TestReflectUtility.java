/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder.config.reflect;

/**
 *
 * @author HP
 */

import com.mycompany.pathfinder.model.enums.AlgorithmType;
import com.mycompany.pathfinder.model.enums.CellState;
import com.mycompany.pathfinder.model.enums.CellType;
import com.mycompany.pathfinder.models.AlgoRun;
import com.mycompany.pathfinder.models.Grid;
import com.mycompany.pathfinder.models.GridCell;
import com.mycompany.pathfinder.models.PathCell;
import java.util.List;

public class TestReflectUtility {

    /*public static void main(String[] args) {

        System.out.println("=== TEST SAUVEGARDE GRID ===");

        try {

            // Création d'une grille
            Grid grid = new Grid(8, 8);

            System.out.println("Grille créée : " + grid);

            // Sauvegarde dans PostgreSQL
            int gridId = ReflectUtility.insertAndGetId(
                    grid,
                    "grid",
                    "grid_id"
            );

            System.out.println(
                    "Grille sauvegardée avec ID : " + gridId
            );

            // Vérification : lecture depuis PostgreSQL
            Grid savedGrid = ReflectUtility.selectById(
                    "grid",
                    "grid_id",
                    gridId,
                    Grid.class
            );

            System.out.println(
                    "Grille récupérée depuis PostgreSQL : "
                    + savedGrid
            );

            System.out.println("=== TEST REUSSI ===");

        } catch (Exception e) {

            System.out.println("=== ERREUR ===");
            e.printStackTrace();
        }
    }*/
   /* public static void main(String[] args) {

        System.out.println("=== TEST GRID CELL ===");

        try {

            // ID de la grille existante dans PostgreSQL
            int gridId = 1;

            // ==========================================
            // CASE DE DEPART
            // ==========================================

            GridCell startCell = new GridCell(
                    gridId,
                    0,
                    0,
                    CellType.START
            );

            int startId = ReflectUtility.insertAndGetId(
                    startCell,
                    "grid_cell",
                    "cell_id"
            );

            System.out.println(
                    "Case START sauvegardée. ID = "
                    + startId
            );

            // ==========================================
            // CASE D'ARRIVEE
            // ==========================================

            GridCell endCell = new GridCell(
                    gridId,
                    7,
                    7,
                    CellType.END
            );

            int endId = ReflectUtility.insertAndGetId(
                    endCell,
                    "grid_cell",
                    "cell_id"
            );

            System.out.println(
                    "Case END sauvegardée. ID = "
                    + endId
            );

            // ==========================================
            // CASE MUR
            // ==========================================

            GridCell wallCell = new GridCell(
                    gridId,
                    3,
                    3,
                    CellType.WALL
            );

            int wallId = ReflectUtility.insertAndGetId(
                    wallCell,
                    "grid_cell",
                    "cell_id"
            );

            System.out.println(
                    "Case WALL sauvegardée. ID = "
                    + wallId
            );

            // ==========================================
            // LECTURE DES CASES
            // ==========================================

            System.out.println("\n=== CASES DE LA GRILLE ===");

            List<GridCell> cells =
                    ReflectUtility.select(
                            "grid_cell",
                            "grid_id = " + gridId,
                            GridCell.class
                    );

            for (GridCell cell : cells) {
                System.out.println(cell);
            }

            System.out.println("\n=== TEST REUSSI ===");

        } catch (Exception e) {

            System.out.println("\n=== ERREUR ===");
            e.printStackTrace();
        }
    }*/
     /*public static void main(String[] args) {

        System.out.println("=== TEST ALGO RUN ===");

        try {

            // ID d'une grille déjà présente dans la base
            int gridId = 1;

            // Simulation d'une exécution de A*
            AlgoRun run = new AlgoRun(
                    gridId,
                    AlgorithmType.ASTAR,
                    2.35,
                    42,
                    15
            );

            System.out.println(
                    "Exécution créée : " + run
            );

            // Sauvegarde dans PostgreSQL
            int runId = ReflectUtility.insertAndGetId(
                    run,
                    "algo_run",
                    "run_id"
            );

            System.out.println(
                    "Exécution sauvegardée. ID = "
                    + runId
            );

            // Récupération depuis PostgreSQL
            AlgoRun savedRun =
                    ReflectUtility.selectById(
                            "algo_run",
                            "run_id",
                            runId,
                            AlgoRun.class
                    );

            System.out.println(
                    "Exécution récupérée : "
                    + savedRun
            );

            System.out.println(
                    "\n=== TEST REUSSI ==="
            );

        } catch (Exception e) {

            System.out.println(
                    "\n=== ERREUR ==="
            );

            e.printStackTrace();
        }
    }*/
    /* public static void main(String[] args) {

        System.out.println("=== TEST PATH CELL ===");

        try {

            // ID d'une exécution déjà présente dans la base
            int runId = 1;

            // ==========================================
            // CASE EXPLOREE
            // ==========================================

            PathCell exploredCell = new PathCell(
                    runId,
                    1,
                    1,
                    CellState.EXPLORED
            );

            int exploredId =
                    ReflectUtility.insertAndGetId(
                            exploredCell,
                            "path_cell",
                            "path_cell_id"
                    );

            System.out.println(
                    "Case EXPLORED sauvegardée. ID = "
                    + exploredId
            );

            // ==========================================
            // CASE DU CHEMIN
            // ==========================================

            PathCell pathCell = new PathCell(
                    runId,
                    2,
                    2,
                    CellState.PATH
            );

            int pathId =
                    ReflectUtility.insertAndGetId(
                            pathCell,
                            "path_cell",
                            "path_cell_id"
                    );

            System.out.println(
                    "Case PATH sauvegardée. ID = "
                    + pathId
            );

            // ==========================================
            // LECTURE
            // ==========================================

            System.out.println(
                    "\n=== CASES DE L'EXECUTION ==="
            );

            var cells =
                    ReflectUtility.select(
                            "path_cell",
                            "run_id = " + runId,
                            PathCell.class
                    );

            for (PathCell cell : cells) {
                System.out.println(cell);
            }

            System.out.println(
                    "\n=== TEST REUSSI ==="
            );

        } catch (Exception e) {

            System.out.println(
                    "\n=== ERREUR ==="
            );

            e.printStackTrace();
        }
    }*/
      public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("   TEST COMPLET PATHFINDER");
        System.out.println("=================================");

        try {

            // =================================================
            // 1. CREER UNE GRILLE
            // =================================================

            System.out.println("\n[1] Création de la grille...");

            Grid grid = new Grid(8, 8);

            int gridId = ReflectUtility.insertAndGetId(
                    grid,
                    "grid",
                    "grid_id"
            );

            System.out.println(
                    "Grille sauvegardée : ID = " + gridId
            );


            // =================================================
            // 2. CREER LES CASES IMPORTANTES
            // =================================================

            System.out.println("\n[2] Création des cases...");

            // Départ
            GridCell start = new GridCell(
                    gridId,
                    0,
                    0,
                    CellType.START
            );

            int startId =
                    ReflectUtility.insertAndGetId(
                            start,
                            "grid_cell",
                            "cell_id"
                    );

            // Arrivée
            GridCell end = new GridCell(
                    gridId,
                    7,
                    7,
                    CellType.END
            );

            int endId =
                    ReflectUtility.insertAndGetId(
                            end,
                            "grid_cell",
                            "cell_id"
                    );

            // Mur
            GridCell wall = new GridCell(
                    gridId,
                    3,
                    3,
                    CellType.WALL
            );

            int wallId =
                    ReflectUtility.insertAndGetId(
                            wall,
                            "grid_cell",
                            "cell_id"
                    );

            System.out.println(
                    "START sauvegardé : " + startId
            );

            System.out.println(
                    "END sauvegardé : " + endId
            );

            System.out.println(
                    "WALL sauvegardé : " + wallId
            );


            // =================================================
            // 3. CREER UNE EXECUTION D'ALGORITHME
            // =================================================

            System.out.println(
                    "\n[3] Création de l'exécution A*..."
            );

            AlgoRun run = new AlgoRun(
                    gridId,
                    AlgorithmType.ASTAR,
                    2.35,
                    42,
                    15
            );

            int runId =
                    ReflectUtility.insertAndGetId(
                            run,
                            "algo_run",
                            "run_id"
                    );

            System.out.println(
                    "Exécution sauvegardée : ID = "
                    + runId
            );


            // =================================================
            // 4. SAUVEGARDER DES CASES EXPLOREES
            // =================================================

            System.out.println(
                    "\n[4] Sauvegarde des cases explorées..."
            );

            PathCell explored1 = new PathCell(
                    runId,
                    0,
                    0,
                    CellState.EXPLORED
            );

            PathCell explored2 = new PathCell(
                    runId,
                    0,
                    1,
                    CellState.EXPLORED
            );

            PathCell explored3 = new PathCell(
                    runId,
                    1,
                    1,
                    CellState.EXPLORED
            );

            ReflectUtility.insertAndGetId(
                    explored1,
                    "path_cell",
                    "path_cell_id"
            );

            ReflectUtility.insertAndGetId(
                    explored2,
                    "path_cell",
                    "path_cell_id"
            );

            ReflectUtility.insertAndGetId(
                    explored3,
                    "path_cell",
                    "path_cell_id"
            );


            // =================================================
            // 5. SAUVEGARDER LE CHEMIN FINAL
            // =================================================

            System.out.println(
                    "\n[5] Sauvegarde du chemin..."
            );

            PathCell path1 = new PathCell(
                    runId,
                    0,
                    0,
                    CellState.PATH
            );

            PathCell path2 = new PathCell(
                    runId,
                    1,
                    1,
                    CellState.PATH
            );

            PathCell path3 = new PathCell(
                    runId,
                    2,
                    2,
                    CellState.PATH
            );

            PathCell path4 = new PathCell(
                    runId,
                    3,
                    3,
                    CellState.PATH
            );

            ReflectUtility.insertAndGetId(
                    path1,
                    "path_cell",
                    "path_cell_id"
            );

            ReflectUtility.insertAndGetId(
                    path2,
                    "path_cell",
                    "path_cell_id"
            );

            ReflectUtility.insertAndGetId(
                    path3,
                    "path_cell",
                    "path_cell_id"
            );

            ReflectUtility.insertAndGetId(
                    path4,
                    "path_cell",
                    "path_cell_id"
            );


            // =================================================
            // 6. VERIFICATION
            // =================================================

            System.out.println(
                    "\n================================="
            );

            System.out.println(
                    "      VERIFICATION"
            );

            System.out.println(
                    "=================================");


            // Grille
            Grid savedGrid =
                    ReflectUtility.selectById(
                            "grid",
                            "grid_id",
                            gridId,
                            Grid.class
                    );

            System.out.println(
                    "\nGRID :"
            );

            System.out.println(savedGrid);


            // Cases
            List<GridCell> cells =
                    ReflectUtility.select(
                            "grid_cell",
                            "grid_id = " + gridId,
                            GridCell.class
                    );

            System.out.println(
                    "\nGRID CELLS : "
                    + cells.size()
            );

            for (GridCell cell : cells) {
                System.out.println(cell);
            }


            // Exécution
            AlgoRun savedRun =
                    ReflectUtility.selectById(
                            "algo_run",
                            "run_id",
                            runId,
                            AlgoRun.class
                    );

            System.out.println(
                    "\nALGO RUN :"
            );

            System.out.println(savedRun);


            // Path
            List<PathCell> pathCells =
                    ReflectUtility.select(
                            "path_cell",
                            "run_id = " + runId,
                            PathCell.class
                    );

            System.out.println(
                    "\nPATH CELLS : "
                    + pathCells.size()
            );

            for (PathCell pathCell : pathCells) {
                System.out.println(pathCell);
            }


            // =================================================
            // FIN
            // =================================================

            System.out.println(
                    "\n================================="
            );

            System.out.println(
                    "      TEST COMPLET REUSSI"
            );

            System.out.println(
                    "=================================");

        } catch (Exception e) {

            System.out.println(
                    "\n================================="
            );

            System.out.println(
                    "          ERREUR"
            );

            System.out.println(
                    "================================="
            );

            e.printStackTrace();
        }
    }
}
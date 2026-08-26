/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pathfinder;

/**
 *
 * @author Admin
 */




import com.mycompany.pathfinder.grid.Cell;
import com.mycompany.pathfinder.grid.Grid;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class MainController {

    @FXML
    private GridPane gridPane;

    private Grid grid;

    @FXML
    public void initialize() {
        System.out.println("MainController chargé !");

        grid = new Grid();

        createGrid();
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

                visualCell.setOnMouseClicked(event -> {

                    modelCell.toggleWall();

                    updateCellStyle(visualCell, modelCell);
                });

                gridPane.add(visualCell, column, row);
            }
        }
    }

    private void updateCellStyle(Region visualCell, Cell cell) {

        if (cell.isWall()) {

            visualCell.setStyle(
                "-fx-background-color: darkgray;" +
                "-fx-border-color: lightgray;" +
                "-fx-border-width: 0.5;"
            );

        } else {

            visualCell.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: lightgray;" +
                "-fx-border-width: 0.5;"
            );
        }
    }
}

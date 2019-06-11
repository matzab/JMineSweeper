package sample;

/* * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Created by: matej
 * Time of the creation: 2:30 PM
 * Day of the creation: 6/9/2019
 *
 * * * * * * * * * * * * * * * * * * * * * * * *
 */

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Grid {
    private int totalBombs = 10;
    public static final int CELL_WIDTH = 20;
    public static int GRID_WIDTH = 161;
    private int columns;
    private int rows;
    private Cell[][] field;


    public Grid(AnchorPane anchorPane) {
        rows = (int) Math.floor(GRID_WIDTH / CELL_WIDTH);
        columns = (int) Math.floor(GRID_WIDTH / CELL_WIDTH);
        field = new Cell[rows][columns];
        Canvas canvas = new Canvas(GRID_WIDTH, GRID_WIDTH);
        canvas.setLayoutX(20);
        canvas.setLayoutY(20);
        setupGrid();
        setupCanvas(canvas);
        anchorPane.getChildren().add(canvas);
    }

    private void setupGrid() {
        ArrayList<Pair> bombs = new ArrayList<>();
        Random random = new Random();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                bombs.add(new Pair(x, y));
            }
        }

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                Cell cell = new Cell(x * CELL_WIDTH, y * CELL_WIDTH, CELL_WIDTH);
                field[y][x] = cell;
            }
        }

        for (int c = 0; c < totalBombs; c++) {
            int index = random.nextInt(bombs.size());
            Pair pair = bombs.get(index);
            field[pair.column][pair.row].setBomb(true);
            bombs.remove(pair);
        }

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                field[y][x].countNeighbourBombs(field);
            }
        }
    }

    private void setupCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Stack<Cell> cells = new Stack<>();

        canvas.setOnMouseMoved(event -> {
            double x = event.getX();
            double y = event.getY();
            if (!cells.isEmpty()) {
                Cell tmp = cells.pop();
                clearHighlight(gc, tmp, x, y);
            }
            Cell cell = currentCell(x, y);
            cell.highLightCell(gc);
//            System.out.println("Current cell X " + cell.getX()+ " Y " + cell.getY());
            cells.push(cell);
//            System.out.println();
        });

        canvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();

            Cell cell = currentCell(x, y);
            MouseButton mouseButton = event.getButton();
            switch (mouseButton) {
                case PRIMARY:
                    cell.openCell(field, gc);
                    break;
                case SECONDARY:
                    break;
            }
        });

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                Cell cell = field[y][x];
                gc.strokeRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getWidth());
            }
        }
    }

    private void clearHighlight(GraphicsContext gc, Cell cell, double x, double y) {
//        System.out.println("Previous cell X " + cell.getX()+ " Y " + cell.getY());
//        System.out.println("Current mouse X " + x + " Y " + y);
        if (x <= cell.getX() || x >= cell.getX() + cell.getWidth() || y <= cell.getY() || y >= cell.getY() + cell.getWidth()) {
//            System.out.println("CHANGED CELL");
            gc.setStroke(Color.BLACK);
            gc.strokeRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getWidth());
        }
    }

    private Cell currentCell(double x, double y) {
        int row, col;
        if (x < CELL_WIDTH && y < CELL_WIDTH) {
            col = 0;
            row = 0;
        } else if (y == GRID_WIDTH - 1 && x == GRID_WIDTH - 1) {
            col = (int) Math.floor(x / CELL_WIDTH) - 1;
            row = (int) Math.floor(y / CELL_WIDTH) - 1;
        } else if (x == GRID_WIDTH - 1) {
            col = (int) Math.floor(x / CELL_WIDTH) - 1;
            row = (int) Math.floor(y / CELL_WIDTH);
        } else if (y == GRID_WIDTH - 1) {
            col = (int) Math.floor(x / CELL_WIDTH);
            row = (int) Math.floor(y / CELL_WIDTH) - 1;
        } else {
            col = (int) Math.floor(x / CELL_WIDTH);
            row = (int) Math.floor(y / CELL_WIDTH);
        }
//        System.out.println("Row " + row + " Column " + col);
        return field[row][col];
    }

    private class Pair {
        int column, row;

        private Pair(int column, int row) {
            this.column = column;
            this.row = row;
        }
    }
}

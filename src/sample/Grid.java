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
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


/**
 *
 */


public class Grid {
    private int totalBombs, nFlags;
    private int CELL_WIDTH;
    private int GRID_WIDTH;
    private int columns;
    private int rows;
    private Cell[][] field;
    private boolean gameOver;
    private Canvas canvas;
    private GraphicsContext gc;
    private Label flagLabel, gameOverLabel;

    public Grid(Canvas canvas, Difficulty difficulty, Label flagLabel, Label gameOverLabel) {
        this.CELL_WIDTH = difficulty.getCellWidth();
        this.totalBombs = difficulty.getnBombs();
        this.GRID_WIDTH = difficulty.getGridWidth();
        nFlags = totalBombs;
        rows = (int) Math.floor(GRID_WIDTH / CELL_WIDTH);
        columns = (int) Math.floor(GRID_WIDTH / CELL_WIDTH);
        field = new Cell[rows][columns];
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        setupGrid();
        setupCanvas(canvas);
        gameOver = false;
        this.flagLabel = flagLabel;
        this.gameOverLabel = gameOverLabel;
        this.gameOverLabel.setVisible(false);
        updateLabel(nFlags, totalBombs);
    }

    private void setupGrid() {
        ArrayList<Pair> bombs = new ArrayList<>();
        Random random = new Random();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                bombs.add(new Pair(y, x));
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
            field[pair.row][pair.column].setBomb(true);
            bombs.remove(pair);
        }

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                countNeighbourBombs(field[y][x]);
            }
        }
    }

    private void setupCanvas(Canvas canvas) {
        Stack<Cell> cells = new Stack<>();

        canvas.setOnMouseMoved(event -> {

            double x = event.getX();
            double y = event.getY();

            if (!cells.isEmpty()) {
                Cell tmp = cells.pop();
                tmp.clearHighlight(gc, x, y);
            }
            Cell cell = currentCell(x, y);
            cell.highLightCell(gc);
//            System.out.println("Current cell X " + cell.getX()+ " Y " + cell.getY());
            cells.push(cell);
//            System.out.println();
        });

        canvas.setOnMouseClicked(event -> {
            if (gameOver) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            Cell cell = currentCell(x, y);
            MouseButton mouseButton = event.getButton();
            switch (mouseButton) {
                case PRIMARY:
                    openCell(cell);
                    if (gameOver) {
                        gameOverLabel.setVisible(true);
                        return;
                    }

                    updateLabel(nFlags, totalBombs);
                    break;
                case SECONDARY:
                    if (!cell.isFlagged() && nFlags > 0) {
                        cell.drawFlag(gc);
                        nFlags--;
                    } else {
                        if (cell.isFlagged() && !cell.isOpened()) {
                            nFlags++;
                            cell.drawFlag(gc);
                        }
                    }
                    updateLabel(nFlags, totalBombs);
                    break;
            }
        });

        gc.setStroke(Color.BLACK);
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                Cell cell = field[y][x];
                gc.strokeRect(cell.getX(), cell.getY(), cell.getWidth(), cell.getWidth());
            }
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

    private void updateLabel(int remainingFlags, int maxMaxFlags) {
        flagLabel.setText(String.format("%d/%02d ", remainingFlags, maxMaxFlags));
    }

    public void clearGrid() {
        canvas.getGraphicsContext2D().clearRect(0, 0, GRID_WIDTH, GRID_WIDTH);
    }

    private void openCell(Cell cell) {
        cell.setOpened(true);
        if (cell.isBomb()) {
            gameOver = true;
            return;
        }
        cell.drawCell(gc);
        if (cell.getNeighbourBombs() == 0) {
            floodFillNeighbours(cell);
        }
    }

    private void floodFillNeighbours(Cell cell) {

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                int xoff = x + cell.getCol();
                int yoff = y + cell.getRow();
                if (xoff >= 0 && xoff < field.length && yoff >= 0 && yoff < field.length) {
                    Cell tmpCell = field[yoff][xoff];
                    if (!tmpCell.isOpened()) {
                        if (tmpCell.isFlagged()) {
                            tmpCell.setFlagged(false);
                            nFlags++;
                        }
                        openCell(tmpCell);
                    }
                }
            }
        }
    }

    private void countNeighbourBombs(Cell cell) {
        int c = 0;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                int xoff = x + cell.getCol();
                int yoff = y + cell.getRow();
                if (xoff >= 0 && xoff < field.length && yoff >= 0 && yoff < field.length) {
                    if (field[yoff][xoff].isBomb()) {
                        c++;
                    }
                }
            }
        }
        cell.setNeighbourBombs(c);
    }

    private class Pair {
        int column, row;

        private Pair(int row, int column) {
            this.column = column;
            this.row = row;
        }
    }
}

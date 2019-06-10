package sample;

/* * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Created by: matej
 * Time of the creation: 2:18 PM
 * Day of the creation: 6/9/2019
 *
 * * * * * * * * * * * * * * * * * * * * * * * *
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cell {
    private boolean opened = false;
    private boolean bomb = false;
    private int col, row;
    private double x, y, width;
    private int neighbourBombs;

    public Cell(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
        row = (int) Math.floor(y / Grid.CELL_WIDTH);
        col = (int) Math.floor(x / Grid.CELL_WIDTH);
        neighbourBombs = 0;
    }

    public void drawCell(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        if (isBomb()) {
            gc.fillOval(getX() + Grid.CELL_WIDTH * .25, getY() + Grid.CELL_WIDTH * .25, 10, 10);
        } else {
            String s = "";
            if (neighbourBombs != 0) {
                s = String.valueOf(neighbourBombs);
            }
            gc.fillRect(getX(), getY(), getWidth(), getWidth());
            gc.strokeText(s, getX() + Grid.CELL_WIDTH * .33, getY() + Grid.CELL_WIDTH * .7);
        }
    }

    public boolean isOpened() {
        return opened;
    }

    public void openCell(Cell[][] field, GraphicsContext gc) {
        opened = true;
        drawCell(gc);
        if (neighbourBombs == 0) {
            floodFillNeighbours(gc, field);
        }
    }

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int countNeighbourBombs(Cell[][] field) {
        int c = 0;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                int xoff = x + col;
                int yoff = y + row;
                if (xoff >= 0 && xoff < field.length && yoff >= 0 && yoff < field.length) {
                    if (field[yoff][xoff].isBomb()) {
                        c++;
                    }
                }
            }
        }
        neighbourBombs = c;
        return c;
    }

    private void floodFillNeighbours(GraphicsContext gc, Cell[][] field) {

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                int xoff = x + col;
                int yoff = y + row;
                if (xoff >= 0 && xoff < field.length && yoff >= 0 && yoff < field.length) {
                    Cell cell = field[yoff][xoff];
                    if (!cell.isOpened()) {
                        cell.openCell(field, gc);
                    }
                }
            }
        }
    }
}

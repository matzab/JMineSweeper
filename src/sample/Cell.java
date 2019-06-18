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
import javafx.scene.text.Font;

public class Cell {
    private boolean opened, bomb, flagged;
    private int col, row, neighbourBombs;
    private double x, y, width;

    public Cell(double x, double y, double width) {
        this.opened = false;
        this.bomb = false;
        this.flagged = false;
        this.x = x;
        this.y = y;
        this.width = width;
        row = (int) Math.floor(y / this.width);
        col = (int) Math.floor(x / this.width);
        neighbourBombs = 0;
    }

    public void drawCell(GraphicsContext gc) {
        gc.setFont(Font.font(width / 2));
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.rgb(150, 150, 150));
        if (isBomb()) {
            gc.setFill(Color.BLACK);
            gc.fillOval(getX() + width * .25, getY() + width * .25, width / 2, width / 2);
        } else {
            String s = "";
            if (neighbourBombs != 0) {
                s = String.valueOf(neighbourBombs);
                gc.setFill(Color.rgb(127, 127, 127));
            }
            gc.fillRect(getX(), getY(), getWidth() - 1, getWidth() - 1);
            gc.strokeRect(getX(), getY(), getWidth(), getWidth());
            gc.strokeText(s, getX() + width * .33, getY() + width * .7);
        }
    }

    public void drawFlag(GraphicsContext gc) {
        if (!isOpened() && !isFlagged()) {
            setFlagged(true);
            gc.setStroke(Color.RED);
            gc.strokeLine(getX(), getY(), getX() + getWidth(), getY() + getWidth());
            gc.strokeLine(getX() + getWidth(), getY(), getX(), getY() + getWidth());
        } else if (!isOpened() && isFlagged()) {
            setFlagged(false);
            gc.setFill(Color.WHITE);
            gc.fillRect(getX(), getY(), getWidth() - 1, getWidth() - 1);
        }
    }

    public void highLightCell(GraphicsContext gc) {
        gc.setStroke(Color.VIOLET);
        gc.strokeRect(getX(), getY(), getWidth(), getWidth());
    }

    public void clearHighlight(GraphicsContext gc, double x, double y) {
//        System.out.println("Previous cell X " + cell.getX()+ " Y " + cell.getY());
//        System.out.println("Current mouse X " + row + " Y " + column);
        if (x <= getX() || x >= getX() + getWidth() || y <= getY() || y >= getY() + getWidth()) {
//            System.out.println("CHANGED CELL");
            gc.setStroke(Color.BLACK);
            gc.strokeRect(getX(), getY(), getWidth(), getWidth());
        }
    }

    public boolean isOpened() {
        return opened;
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

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public int getNeighbourBombs() {
        return neighbourBombs;
    }

    public void setNeighbourBombs(int neighbourBombs) {
        this.neighbourBombs = neighbourBombs;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

}

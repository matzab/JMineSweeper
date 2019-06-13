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
    private int col, row, neighbourBombs, rClickCnt;
    private double x, y, width;

    public Cell(double x, double y, double width) {
        this.opened = false;
        this.bomb = false;
        this.flagged = false;
        this.x = x;
        this.y = y;
        this.width = width;
        row = (int) Math.floor(y / Grid.CELL_WIDTH);
        col = (int) Math.floor(x / Grid.CELL_WIDTH);
        neighbourBombs = 0;
        rClickCnt = 0;
    }

    private void drawCell(GraphicsContext gc) {
        gc.setFont(Font.font(Grid.CELL_WIDTH / 2));
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.rgb(150, 150, 150));
        if (isBomb()) {
            gc.setFill(Color.BLACK);
            gc.fillOval(getX() + Grid.CELL_WIDTH * .25, getY() + Grid.CELL_WIDTH * .25, Grid.CELL_WIDTH / 2, Grid.CELL_WIDTH / 2);
        } else {
            String s = "";
            if (neighbourBombs != 0) {
                s = String.valueOf(neighbourBombs);
                gc.setFill(Color.rgb(127, 127, 127));
            }
            gc.fillRect(getX(), getY(), getWidth() - 1, getWidth() - 1);
            gc.strokeRect(getX(), getY(), getWidth(), getWidth());
            gc.strokeText(s, getX() + Grid.CELL_WIDTH * .33, getY() + Grid.CELL_WIDTH * .7);
        }
    }

    public void drawFlag(GraphicsContext gc) {
        if (!isOpened() && getrClickCnt() == 0) {
            setFlagged(true);
            gc.setStroke(Color.RED);
            gc.strokeLine(getX(), getY(), getX() + getWidth(), getY() + getWidth());
            gc.strokeLine(getX() + getWidth(), getY(), getX(), getY() + getWidth());
        } else if (!isOpened() && getrClickCnt() == 1) {
            setFlagged(false);
            gc.setFill(Color.WHITE);
            gc.fillRect(getX(), getY(), getWidth() - 1, getWidth() - 1);
        }
        incrementrClickCnt();
    }

    public void highLightCell(GraphicsContext gc) {
        gc.setStroke(Color.rgb(150, 150, 150, 0.5));
        if (isOpened()) {
            gc.setFont(Font.font(Grid.CELL_WIDTH / 2));
            gc.setFill(Color.rgb(150, 150, 150, 0.5));
            if (isBomb()) {
                gc.setFill(Color.BLACK);
                gc.fillOval(getX() + Grid.CELL_WIDTH * .25, getY() + Grid.CELL_WIDTH * .25, Grid.CELL_WIDTH / 2, Grid.CELL_WIDTH / 2);
            } else {
                String s = "";
                if (neighbourBombs != 0) {
                    s = String.valueOf(neighbourBombs);
                    gc.setFill(Color.rgb(127, 127, 127, 0.5));
                }
                gc.fillRect(getX(), getY(), getWidth() - 1, getWidth() - 1);
                gc.setStroke(Color.BLACK);
                gc.strokeText(s, getX() + Grid.CELL_WIDTH * .33, getY() + Grid.CELL_WIDTH * .7);
            }
        } else {
            gc.strokeRect(getX(), getY(), getWidth(), getWidth());
        }
    }

    private int getrClickCnt() {
        return rClickCnt;
    }

    private void incrementrClickCnt() {
        if (!isOpened()) {
            rClickCnt++;
        }
        if (rClickCnt > 1) {
            rClickCnt = 0;
        }
    }


    public boolean isOpened() {
        return opened;
    }

    public boolean openCell(Cell[][] field, GraphicsContext gc) {
        opened = true;
        drawCell(gc);
        if (neighbourBombs == 0) {
            floodFillNeighbours(gc, field);
        }
        return isBomb();
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

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public void countNeighbourBombs(Cell[][] field) {
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
    }

    private void floodFillNeighbours(GraphicsContext gc, Cell[][] field) {

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                int xoff = x + col;
                int yoff = y + row;
                if (xoff >= 0 && xoff < field.length && yoff >= 0 && yoff < field.length) {
                    Cell cell = field[yoff][xoff];
                    if (!cell.isOpened()) {
                        if(cell.isFlagged()){
                            setFlagged(false);
                            Grid.nFlags++;
                        }
                        cell.openCell(field, gc);
                    }
                }
            }
        }
    }
}

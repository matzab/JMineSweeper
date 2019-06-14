package sample;

/**
 * Creator: matej
 * Date: 2019-06-14
 * Time: 19:36
 * <p>
 * Beginner mode: 10 bombs, 8 row 8 grid
 * Intermediate mode: 40 bombs, 16 row 16 gird
 * Expert mode: 99 bombs, 25 row 25 grid
 */
enum Difficulty {
    BEGINNER("BEGINNER",10, 20, 161), INTERMEDIATE("INTERMEDIATE",40, 20, 321), EXPERT("EXPERT",99, 15, 376);

    private int nBombs;
    private int cellWidth;
    private int gridWidth;
    private String name;

    Difficulty(String name, int nBombs, int cellWidth, int gridWidth) {
        this.nBombs = nBombs;
        this.cellWidth = cellWidth;
        this.gridWidth = gridWidth;
        this.name = name;
    }

    public int getnBombs() {
        return nBombs;
    }

    public void setnBombs(int nBombs) {
        this.nBombs = nBombs;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    @Override
    public String toString() {
        int n = (int)Math.floor(gridWidth/cellWidth);
        return String.format("%s (%d row %d, %d)", name,n,n,nBombs);
    }
}

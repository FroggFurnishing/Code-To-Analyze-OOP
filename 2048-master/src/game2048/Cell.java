package game2048;

import java.io.Serializable;

public class Cell implements Serializable {
    private int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell getCellAtDir(Direction dir) {
        return new Cell(x+dir.getX(), y+dir.getY());
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

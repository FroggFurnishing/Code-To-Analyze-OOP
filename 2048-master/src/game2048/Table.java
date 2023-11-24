package game2048;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Table<E> implements Serializable {
    private Object[][] table;
    private int height, width;

    public Table(int width, int height) {
        this.width = width;
        this.height = height;
        table = new Object[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public E set(E e, Cell position) {
        return set(e, position.getX(), position.getY());
    }

    private E set(E e, int x, int y) {
        checkRange(x, y);
        E e2 = get(x, y);
        table[y][x] = e;
        return e2;
    }

    public E get(Cell position) {
        return get(position.getX(), position.getY());
    }

    public E get(int x, int y) {
        checkRange(x, y);
        return (E) table[y][x];
    }

    private void checkRange(int x, int y) {
        if (!isInTable(x, y))
            throw new ArrayIndexOutOfBoundsException("x=" + x + ", y=" + y);
    }

    public boolean isInTable(Cell position) {
        return isInTable(position.getX(), position.getY());
    }

    private boolean isInTable(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public E remove(Cell position) {
        return remove(position.getX(), position.getY());
    }

    private E remove(int x, int y) {
        return set(null, x, y);
    }

    public E move(Cell from, Cell to) {
        return move(from.getX(), from.getY(), to.getX(), to.getY());
    }

    private E move(int x1, int y1, int x2, int y2) {
        E e = set(null, x1, y1);
        return set(e, x2, y2);
    }

    public Cell[] getEmpties() {
        ArrayList<Cell> emptyCells = new ArrayList<>();
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (table[i][j] == null)
                    emptyCells.add(new Cell(j, i));
        return emptyCells.toArray(new Cell[emptyCells.size()]);
    }

    public E[][] toArray(E[][] table) {
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                table[i][j] = (E) this.table[i][j];
        return table;
    }

    public Object[][] splitLines(Direction lineDir) {
        rotate(lineDir.getAngle(Direction.LEFT));
        Object[][] lines = new Object[height][];
        for (int i = 0; i < height; i++) {
            ArrayList<Object> line = new ArrayList<>();
            for (int j = 0; j < width; j++)
                if (table[i][j] != null)
                    line.add(table[i][j]);
            lines[i] = line.toArray();
        }
        rotate(Direction.LEFT.getAngle(lineDir));
        return lines;
    }

    private void rotate(int angle) {
        switch (angle) {
            case 90:
                rotate90();
                break;
            case 180:
                rotate180();
                break;
            case 270:
                rotate270();
                break;
        }
    }

    private void rotate90() {
        Object[][] newTable = new Object[width][height];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                newTable[width - 1 - j][i] = table[i][j];
        table = newTable;
        width = height;
        height = table.length;
    }

    private void rotate180() {
        Object[][] newTable = new Object[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                newTable[height - 1 - i][width - 1 - j] = table[i][j];
        table = newTable;
    }

    private void rotate270() {
        Object[][] newTable = new Object[width][height];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                newTable[j][height - 1 - i] = table[i][j];
        table = newTable;
        width = height;
        height = table.length;
    }

    @Override
    public String toString() {
        String result = new String();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                if (table[i][j] == null)
                    result += "n\t";
                else
                    result += table[i][j] + "\t";
            result += "\n";
        }
        return result;
    }
}

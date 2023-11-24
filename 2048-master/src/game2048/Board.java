package game2048;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    private Table<Tile> tileTable;
    private int width, height;
    private transient ArrayList<Tile> mergedTiles;
    public transient BoardContainer graphics;
    public transient BoardPanel tilePanel;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        tileTable = new Table<>(width, height);
        mergedTiles = new ArrayList<>();
        graphics = new BoardContainer();
        tilePanel = new BoardPanel();
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mergedTiles = new ArrayList<>();
        graphics = new BoardContainer();
        tilePanel = new BoardPanel();
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tileTable.get(j, i) != null)
                    addTile(tileTable.get(j, i));
    }

    public void move(Direction moveDir) {
        mergedTiles.clear();
        Object[][] tiles = tileTable.splitLines(moveDir);
        for (Object[] line : tiles) {
            int filledCells = 0;
            for (int j = 0; j < line.length; j++) {
                Tile tile = (Tile)line[j];
                Tile nextTile = (j < line.length - 1) ? (Tile) line[j + 1] : null;
                if (nextTile != null && tile.getLabel() == nextTile.getLabel()) {
                    merge(tile, nextTile, moveDir, filledCells);
                    j++;
                } else {
                    move(tile, moveDir, filledCells);
                }
                filledCells++;
            }
        }
        tilePanel.queue.complete();
    }

    public boolean canMove(Direction moveDir) {
        Tile[][] tiles = tileTable.toArray(new Tile[height][width]);
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                if (tiles[i][j] != null) {
                    Tile currentTile = tiles[i][j];
                    Cell nextCell = currentTile.getPosition().getCellAtDir(moveDir);
                    if (!tileTable.isInTable(nextCell))
                        continue;
                    Tile nextTile = tileTable.get(nextCell);
                    if (nextTile == null || nextTile.getLabel() == currentTile.getLabel())
                        return true;
                }
        return false;
    }

    private void move(Tile tile, Direction moveDir, int index) {
        // TODO: get position from arguments
        Cell currentPosition = tile.getPosition();
        Cell nextPosition = getCell(tile.getPosition(), moveDir, index);
        tileTable.move(currentPosition, nextPosition);
        tile.moveTo(nextPosition);
        tilePanel.queue.addAnimation(tile.graphics.moveTo(currentPosition, nextPosition));
    }

    private void merge(Tile tile1, Tile tile2, Direction moveDir, int gap) {
        // TODO: get position from arguments
        Cell position = getCell(tile1.getPosition(), moveDir, gap);
        Cell pos1 = tile1.getPosition();
        Cell pos2 = tile2.getPosition();
        tileTable.remove(tile1.getPosition()).moveTo(position);
        tileTable.remove(tile2.getPosition()).moveTo(position);
        addTile(tile1.getLabel() + tile2.getLabel(), position);
        mergedTiles.add(tile1);
        mergedTiles.add(tile2);
        tilePanel.queue.addAnimation(tile1.graphics.mergeTo(pos1, position));
        tilePanel.queue.addAnimation(tile2.graphics.mergeTo(pos2, position));
    }

    public Tile[] getMergedTiles() {
        return mergedTiles.toArray(new Tile[mergedTiles.size()]);
    }

    private Cell getCell(Cell cell, Direction moveDir, int index) {
        // TODO: better implementation?
        int dx = moveDir.getX();
        int dy = moveDir.getY();
        int x = (1 - Math.abs(dx)) * cell.getX() + (dx + 1) / 2 * (tileTable.getWidth() - 1);
        int y = (1 - Math.abs(dy)) * cell.getY() + (dy + 1) / 2 * (tileTable.getHeight() - 1);
        x -= dx * index;
        y -= dy * index;
        return new Cell(x, y);
    }

    public void addRandomTiles(int n) {
        for (int i = 0; i < n; i++)
            addRandomTile(false);
        tilePanel.queue.complete();
    }

    public void addRandomTile() {
        addRandomTile(true);
    }

    private void addRandomTile(boolean animate) {
        Cell[] empties = tileTable.getEmpties();
        int i = (int) (Math.random() * empties.length);
        addTile(2, empties[i]);
        if (animate)
            tilePanel.queue.complete();
    }

    private void addTile(int label, Cell position) {
        addTile(new Tile(label, position));
    }

    private void addTile(Tile tile) {
        tileTable.set(tile, tile.getPosition());
        tilePanel.add(tile.graphics, 0);
        tile.graphics.setup();
        tile.graphics.revalidate();
        tilePanel.queue.addAnimation(tile.graphics.emerge());
    }

    @Override
    public String toString() {
        return tileTable.toString();
    }

    public class BoardContainer extends JPanel {
        private final Color BG_COLOR = new Color(0xbaad9e);
        private final double BORDER = 0.1, RADIUS = 0.05, INNER_BORDER = 0.02;

        public BoardContainer() {
            setLayout(null);
        }

        public void setup() {
            setBackground(getParent().getBackground());
            add(tilePanel);
            refreshBounds();
            tilePanel.setup();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            refreshBounds();
            int size = g.getClipBounds().width;
            int border = (int) (size * BORDER);
            int radius = (int) (RADIUS * size);
            GameFrame.setRenderingHints(g);
            g.setColor(BG_COLOR);
            g.fillRoundRect(border, border, size - 2 * border, size - 2 * border, radius, radius);
        }

        private void refreshBounds() {
            Rectangle bounds = getBounds();
            int border = (int) (bounds.width * (BORDER + INNER_BORDER));
            tilePanel.setBounds(border, border, bounds.width - 2 * border, bounds.height - 2 * border);
        }
    }

    public class BoardPanel extends JPanel {
        public final AnimationListQueue queue = new AnimationListQueue();

        public BoardPanel() {
            setLayout(null);
            setBackground(graphics.BG_COLOR);
        }

        public void setup() {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    Tile cell = new Tile(0, new Cell(j, i));
                    add(cell.graphics);
                    cell.graphics.setup();
                    queue.addAnimation(cell.graphics.appear());
                }
        }

        public int getColumns() {
            return width;
        }

        public int getRows() {
            return height;
        }
    }
}

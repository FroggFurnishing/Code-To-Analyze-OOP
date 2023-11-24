package game2048;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.function.Consumer;

public class Tile implements Serializable {
    private int label;
    private Cell position;
    public transient TilePanel graphics = new TilePanel();

    public Tile(int label, Cell position) {
        this.label = label;
        this.position = position;
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        graphics = new TilePanel();
    }

    public int getLabel() {
        return label;
    }

    public Cell getPosition() {
        return position;
    }

    public void moveTo(Cell position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.valueOf(label);
    }

    public class TilePanel extends JPanel {
        private Color BG_COLOR, TEXT_COLOR;
        private final int EMERGE_DURATION =80, MOVE_DURATION = 60, FPS = 80;
        private final double RADIUS = 0.15, BORDER = 0.07;
        private CenterLabel lbl;
        private boolean isAnimating = false;

        public TilePanel() {
            setLayout(null);
            setVisible(false);
        }

        public void setup() {
            setBackground(getParent().getBackground());
            BG_COLOR = Tiles.getTileColor(label);
            TEXT_COLOR = Tiles.getTextColor(label);
            String lblText = String.valueOf(label);
            lbl = new CenterLabel(lblText, 3.0 / Math.max(lblText.length() + 1, 3), TEXT_COLOR, this::scaleFont);
            add(lbl);
            refreshBounds();
        }

        public Font scaleFont(double scale) {
            return new Font("Calibri", Font.BOLD, (int) (0.6 * getWidth() * scale));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            refreshBounds();
            int size = getWidth();
            int border = (int)(size * BORDER);
            GameFrame.setRenderingHints(g);
            g.setColor(BG_COLOR);
            g.fillRoundRect(border, border, size-2*border, size-2*border,
                    (int) (RADIUS * size), (int) (RADIUS * size));
        }

        public void refreshBounds() {
            Rectangle bounds = getPanelBounds();
            if (isAnimating)
                bounds = getBounds();
            else
                setBounds(bounds);
            lbl.setBounds(0, 0, bounds.width, bounds.height);
        }

        private Rectangle getPanelBounds() {
            return getPanelBounds(position);
        }

        private Rectangle getPanelBounds(Cell position) {
            int size = getParent().getWidth() / getColumns();
            int x = position.getX() * size;
            int y = position.getY() * size;
            return new Rectangle(x, y, size, size);
        }

        private int getColumns() {
            return ((Board.BoardPanel)getParent()).getColumns();
        }

        public MoveAnimation emerge() {
            final Cell cell = position;
            final Rectangle s = new Rectangle(), e = new Rectangle();
            Consumer<Double> refreshBounds = t -> {
                e.setBounds(getPanelBounds(cell));
                s.setBounds(e.x + e.width / 2, e.y + e.height / 2, 0, 0);
                refreshBounds();
            };
            return new MoveAnimation(this, EMERGE_DURATION, FPS, () -> {isAnimating = true; setVisible(true);}, s, e,
                    MoveAnimation.MoveType.LINEAR, refreshBounds, () -> isAnimating = false);
        }

        public MoveAnimation appear() {
            setBounds(getPanelBounds());
            refreshBounds();
            setVisible(true);
            return null;
        }

        public MoveAnimation moveTo(Cell start, Cell end) {
            final Rectangle s = new Rectangle(), e = new Rectangle();
            Consumer<Double> refreshBounds = aDouble -> {
                s.setBounds(getPanelBounds(start));
                e.setBounds(getPanelBounds(end));
                refreshBounds();
            };
            return new MoveAnimation(this, MOVE_DURATION, FPS, () -> isAnimating = true, s, e,
                    MoveAnimation.MoveType.LINEAR, refreshBounds, () -> isAnimating = false);
        }

//        public MoveAnimation fade() {
//            final Rectangle e = new Rectangle(), s = new Rectangle();
//            Consumer<Double> refreshBounds = aDouble -> {
//                s.setBounds(getPanelBounds());
//                e.setBounds(s.x + s.width / 2, s.y + s.height / 2, 0, 0);
//                refreshBounds();
//            };
//            return new MoveAnimation(this, 200, FPS, null, s, e,
//                    MoveAnimation.MoveType.LINEAR, refreshBounds, () -> getParent().remove(this));
//        }

        public MoveAnimation mergeTo(Cell start, Cell end) {
            final Rectangle s = new Rectangle(), e = new Rectangle();
            Consumer<Double> refreshBounds = aDouble -> {
                s.setBounds(getPanelBounds(start));
                e.setBounds(getPanelBounds(end));
                refreshBounds();
            };
            return new MoveAnimation(this, MOVE_DURATION, FPS, () -> isAnimating = true, s, e,
                    MoveAnimation.MoveType.LINEAR, refreshBounds, () -> {
                isAnimating = false; getParent().remove(this);});
        }
    }
}

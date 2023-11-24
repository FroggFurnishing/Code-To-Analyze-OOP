package game2048;

import javax.swing.*;
import java.awt.*;

public class Game {
    private Board board;
    private int score, highScore;
    private boolean gameOver;
    public final GamePanel graphics = new GamePanel();

    public Game(int width, int height) {
        board = new Board(width, height);
        score = 0;
        highScore = 0;
        board.addRandomTiles(2);
    }

    public Game(int size) {
        this(size, size);
    }

    public Game() {
        this(4);
    }

    public Game(SaveLoad state) {
        score = state.score;
        highScore = state.highScore;
        board = state.board;
    }

    public void start() {
        new GameFrame(this);
        graphics.setup();
        board.tilePanel.queue.complete();
    }

    public synchronized void nextTurn(Direction dir) {
        if (!board.canMove(dir))
            return;
        board.move(dir);
        for (Tile tile : board.getMergedTiles())
            score += tile.getLabel();
        if (score > highScore)
            highScore = score;
        board.addRandomTile();
    }


    public boolean canMove() {
        for (Direction direction : Direction.values())
            if (board.canMove(direction))
                return true;
        return false;
    }

    public void saveAndExit() {
        SaveLoad.save(score, highScore, board);
        System.exit(0);
    }

    @Override
    public String toString() {
        return board.toString() + "Score: " + score;
    }

    public class GamePanel extends JPanel {
        private final Color BG_COLOR = new Color(250, 248, 238);
        private final double splitRatio = 0.8;
        private final TopPanel topPanel = new TopPanel();

        public GamePanel() {
            setLayout(null);
            setBackground(BG_COLOR);
        }

        public void setup() {
            add(topPanel);
            add(board.graphics);
            refreshBounds();
            topPanel.setup();
            board.graphics.setup();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            refreshBounds();
        }

        private void refreshBounds() {
            // parent size
            Dimension pSize = getParent().getBounds().getSize();
            int width = (int) Math.min(pSize.width, splitRatio*pSize.height);
            int height = (int) (width/splitRatio);
            int hBorder = (pSize.width - width) / 2;
            int vBorder = (pSize.height - height) / 2;
            setBounds(hBorder, vBorder, width, height);
            topPanel.setBounds(0, 0, width, (int)(height*(1-splitRatio)));
            board.graphics.setBounds(0, (int)(height*(1-splitRatio)), width, (int)(height*splitRatio));
        }
    }

    public class TopPanel extends JPanel {
        private final Color TEXT_COLOR = new Color(0x8f7a66),
                SCORE_BG_COLOR = new Color(0xbbada0),
                SCORE_TEXT_COLOR = new Color(0xeee4da);
        private final double SCORE_BOARD_RADIUS = 0.05;
        private CenterLabel title, scoreText, scoreNum, hScoreText, hScoreNum;

        public TopPanel() {
            setLayout(null);

            title = new CenterLabel("2048", 1, TEXT_COLOR, this::scaledFont);
            add(title);

            scoreText = new CenterLabel("SCORE", 0.15, SCORE_TEXT_COLOR, this::scaledFont);
            add(scoreText);

            scoreNum = new CenterLabel(String.valueOf(score), 0.3, Color.white, (t)->scaledScoreFont(t, score));
            add(scoreNum);

            hScoreText = new CenterLabel("BEST", 0.15, SCORE_TEXT_COLOR, this::scaledFont);
            add(hScoreText);

            hScoreNum = new CenterLabel(String.valueOf(highScore), 0.3, Color.white, (t)->scaledScoreFont(t, highScore));
            add(hScoreNum);
        }

        public void setup() {
            setBackground(getParent().getBackground());
            refreshBounds();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            refreshBounds();
            int width = getWidth();
            int height = getHeight();
            int radius = (int) (SCORE_BOARD_RADIUS*width);
            GameFrame.setRenderingHints(g);
            g.setColor(SCORE_BG_COLOR);
            g.fillRoundRect(width/2+width/60, height/6,
                    width/4-width/30, height-height/3,
                    radius, radius);
            g.fillRoundRect(3*width/4+width/60, height/6,
                    width/4-width/30, height-height/3,
                    radius, radius);
            // update score and high score
            scoreNum.setText(String.valueOf(score));
            hScoreNum.setText(String.valueOf(highScore));
        }

        private void refreshBounds() {
            int width = getWidth();
            int height = getHeight();
            title.setBounds(0, 0, width / 2, height);
            scoreText.setBounds(width / 2, height / 6, width / 4, height / 4);
            hScoreText.setBounds(3 * width / 4, height / 6, width / 4, height / 4);
            scoreNum.setBounds(width / 2, 5 * height / 12, width / 4, 5 * height / 12);
            hScoreNum.setBounds(3*width/4, 5*height/12, width/4, 5*height/12);
        }

        public Font scaledFont(double scale) {
            return new Font("Calibri", Font.BOLD, (int) (0.2 * getWidth() * scale));
        }

        public Font scaledScoreFont(double scale, int score) {
            return new Font("Calibri", Font.BOLD, (int) (0.2 * getWidth() * scale / Math.max(String.valueOf(score).length()-4, 1)));
        }
    }
}

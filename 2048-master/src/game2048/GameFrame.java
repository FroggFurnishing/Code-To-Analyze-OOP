package game2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class <code>GameFrame</code> is a <code>JFrame</code> to show contents of
 * the game. It has a timer which refreshes the view of the game.
 */
public class GameFrame extends JFrame {
    private Game game;
//    public final int refreshTime = 25;

    public GameFrame(Game game) {
        this.game = game;
        initUI();
    }

    private void initUI() {
        // main panel is used to show the game
        MainPanel mainPanel = new MainPanel();

        addKeyListener(mainPanel);
        setContentPane(mainPanel);

        setTitle("2048!");
        setSize(350, 500);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.saveAndExit();
            }
        });
        setVisible(true);
    }

    public static void setRenderingHints(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }

    private class MainPanel extends JPanel
            implements ActionListener, KeyListener {
        // timer to refresh main panel
        private final Color BG_COLOR = new Color(220, 220, 220);
//        private Timer mainTimer;

        public MainPanel() {
//            mainTimer = new Timer(refreshTime, this);
//            mainTimer.start();
            setLayout(null);
            add(game.graphics);
        }

        // paint components
        @Override
        protected void paintComponent(Graphics pg) {
            super.paintComponent(pg);
            setBackground(BG_COLOR);
            setRenderingHints(pg);
        }

        // timer will call this function
        @Override
        public void actionPerformed(ActionEvent e) {
//            TODO repaint();
        }

        // this will handle pressing arrows
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    game.nextTurn(Direction.LEFT);
                    break;
                case KeyEvent.VK_UP:
                    game.nextTurn(Direction.TOP);
                    break;
                case KeyEvent.VK_RIGHT:
                    game.nextTurn(Direction.RIGHT);
                    break;
                case KeyEvent.VK_DOWN:
                    game.nextTurn(Direction.BOTTOM);
                    break;
            }
//            repaint();
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}

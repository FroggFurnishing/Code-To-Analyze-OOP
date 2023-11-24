package game2048;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    Game loaded = SaveLoad.load();

    public MainMenu() {
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);

        JButton resumeGame = new JButton("Resume Game");
        resumeGame.setBounds(10, 10, 130, 30);
        resumeGame.addActionListener(e -> {
            setVisible(false);
            loaded.start();
        });
        panel.add(resumeGame);
        if (loaded == null)
            resumeGame.setEnabled(false);

        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> {
            setVisible(false);
            new OptionsFrame().setVisible(true);
        });
        newGame.setBounds(10, 50, 130, 30);
        panel.add(newGame);

        JButton exit = new JButton("Exit");
        exit.setBounds(10, 90, 130, 30);
        exit.addActionListener(e -> System.exit(0));
        exit.setPreferredSize(new Dimension(150, 70));
        panel.add(exit);

        setTitle("2048!");
        setSize(170, 170);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}

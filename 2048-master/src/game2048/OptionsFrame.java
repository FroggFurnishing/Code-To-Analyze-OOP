package game2048;

import javax.swing.*;

public class OptionsFrame extends JFrame {
    public OptionsFrame() {
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(null);

        // width label
        JLabel widthT = new JLabel("width:");
        widthT.setBounds(10, 10, 50, 20);
        panel.add(widthT);
        // width text field
        JTextField width = new JTextField("4");
        width.setBounds(60, 10, 150, 20);
        panel.add(width);

        // height label
        JLabel heightT = new JLabel("height:");
        heightT.setBounds(10, 40, 50, 20);
        panel.add(heightT);
        // height text field
        JTextField height = new JTextField("4");
        height.setBounds(60, 40, 150, 20);
        panel.add(height);

        // start button
        JButton start = new JButton("Start Game");
        start.setBounds(10, 70, 200, 30);
        start.addActionListener((e) -> {
            setVisible(false);
            int widthInt = Integer.parseInt(width.getText());
            int heightInt = Integer.parseInt(height.getText());
            new Game(widthInt, heightInt).start();
        });
        panel.add(start);

        setTitle("Options");
        setSize(240, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

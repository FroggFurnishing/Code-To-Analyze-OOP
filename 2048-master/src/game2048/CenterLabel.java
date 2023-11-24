package game2048;

import javax.swing.*;
import java.awt.*;
import java.util.function.DoubleFunction;

/**
 * Project: game 2048
 * Created by Hadi on 6/2/14 2:54 PM.
 */
public class CenterLabel extends JLabel {
    private double scale;
    private DoubleFunction<Font> fontSupplier;

    public CenterLabel(String text, double scale, Color color, DoubleFunction<Font> fontSupplier) {
        super(text);
        this.scale = scale;
        this.fontSupplier = fontSupplier;
        setForeground(color);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        refreshFont();
        GameFrame.setRenderingHints(g);
    }

    public void refreshFont() {
        Font oldFont = getFont();
        Font newFont = fontSupplier.apply(scale);
        if (!newFont.equals(oldFont))
            setFont(newFont);
    }
}

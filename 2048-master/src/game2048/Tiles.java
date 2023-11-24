package game2048;

import java.awt.*;

public enum Tiles {
    TILE0(0xccc0b3), // Cell is a tile with number 0
    TILE2(0xeee4da),
    TILE4(0xede0c8),
    TILE8(0xf2b179),
    TILE16(0xf59563),
    TILE32(0xf67c5f),
    TILE64(0xf65e3b),
    TILE128(0xedcf72),
    TILE256(0xedcc61),
    TILE512(0xedc850),
    TILE1024(0xedc53f),
    TILE2048(0xedc22e),
    TILE4096(0xcdc1b4),
    TILE8192(0xcdc1b4),
    TILE16384(0xcdc1b4);

    public static Color getTileColor(int label) {
//        System.out.println("TILE" + label + " : " + valueOf("TILE"+label));
        return valueOf("TILE"+label).getTileColor();
    }

    public static Color getTextColor(int label) {
        return valueOf("TILE"+label).getTextColor();
    }


    private Color bgColor, textColor;
    private int label;

    private Tiles(int rgba) {
        this(rgba, false);
    }

    private Tiles(int rgba, boolean hasAlpha) {
        label = Integer.parseInt(name().replaceAll("[^\\d]", ""));
        bgColor = new Color(rgba, hasAlpha);
        if (label == 0)
            textColor = bgColor;
        else if (label < 16)
            textColor = new Color(0x776e65);
        else
            textColor = new Color(0xf9f6f2);
    }

    public Color getTileColor() {
        return bgColor;
    }

    public Color getTextColor() {
        return textColor;
    }
}

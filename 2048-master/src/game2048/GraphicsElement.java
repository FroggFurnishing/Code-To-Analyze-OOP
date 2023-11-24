package game2048;

import java.awt.*;
import java.util.Vector;

public abstract class GraphicsElement {
    protected Vector<GraphicsElement> children;
    protected GraphicsElement parent;
    protected int width, height, x, y;

    public GraphicsElement() {
        parent = null;
        children = new Vector<>();
    }

    private void setParent(GraphicsElement parent) {
        this.parent = parent;
    }

    protected synchronized final void addChild(GraphicsElement e) {
        children.add(children.size(), e);
        System.out.println(children);
        e.setParent(this);
    }

    protected synchronized final void removeChild(GraphicsElement e) {
        children.remove(e);
        e.setParent(null);
    }

    public synchronized final void draw(Graphics2D g) {
        Graphics2D cg = paint(g);
        for (int i = 0; i < children.size(); i++)
            children.get(i).draw(cg);
    }

    public abstract Graphics2D paint(Graphics2D pg);

    public abstract Font getFont(double scale);

    public abstract void setup();
}

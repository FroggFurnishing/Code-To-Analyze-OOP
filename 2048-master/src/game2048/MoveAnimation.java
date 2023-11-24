package game2048;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;

/**
 * Project: game 2048
 * Created by Hadi on 6/2/14 11:40 AM.
 */
public class MoveAnimation extends Thread {
    private JPanel panel;
    private int stepTime, steps;
    private DoubleFunction<Rectangle> function;
    private Consumer<Double> extraAction;
    private Runnable startAction, endAction;
    private boolean isCompleted;

    public MoveAnimation(JPanel panel, int duration, int fps,
                         Runnable startAction,
                         DoubleFunction<Rectangle> function,
                         Consumer<Double> extraAction,
                         Runnable endAction) {
        this.panel = panel;
        steps = duration * fps / 1000;
        stepTime = 1000/fps;
        this.startAction = startAction;
        this.function = function;
        this.extraAction = extraAction;
        this.endAction = endAction;
        isCompleted = false;
    }

    public MoveAnimation(JPanel panel, int duration, int fps,
                         Runnable startAction,
                         Rectangle start, Rectangle end,
                         MoveType type,
                         Consumer<Double> extraAction,
                         Runnable endAction) {
        this(panel, duration, fps, startAction, type.getFunction(start, end), extraAction, endAction);
    }

    @Override
    public void run() {
        if (startAction != null)
            startAction.run();
        for (int step = 0; step <= steps; step++) {
            long startTime = System.currentTimeMillis();
            // do actions
            if (extraAction != null)
                extraAction.accept((double)step/steps);
            Rectangle newBounds = function.apply((double)step/steps);
            panel.setBounds(newBounds);
            // calculate elapsed time
            long endTime = System.currentTimeMillis();
            long remainedTime = stepTime - (endTime - startTime);
            if (remainedTime > 0)
                try {sleep(remainedTime);} catch (InterruptedException e) {e.printStackTrace();}
        }
        endAction.run();
        complete();
    }

    public synchronized void waitToComplete() {
        try {
            while (!isCompleted)
                wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void complete() {
        isCompleted = true;
        notifyAll();
    }

    public enum MoveType {
        LINEAR((s, e) -> t -> getLinearCombination(1-t, s, t, e));

        private BiFunction<Rectangle, Rectangle, DoubleFunction<Rectangle>> function;

        private MoveType(BiFunction<Rectangle, Rectangle, DoubleFunction<Rectangle>> function) {
            this.function = function;
        }

        public DoubleFunction<Rectangle> getFunction(Rectangle start, Rectangle end) {
            return function.apply(start, end);
        }

        private static Rectangle getLinearCombination(double a, Rectangle x, double b, Rectangle y) {
            return new Rectangle(
                    (int)(a*x.x + b*y.x),
                    (int)(a*x.y + b*y.y),
                    (int)(a*x.width + b*y.width),
                    (int)(a*x.height + b*y.height));
        }
    }

//    @Override
//    public String toString() {
//        return "An:" + super.toString();
//    }
}

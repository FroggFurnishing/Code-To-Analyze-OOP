package game2048;

import java.util.ArrayList;

/**
 * Project: game 2048
 * Created by Hadi on 6/2/14 1:21 PM.
 */
public class AnimationList extends Thread {
    private AnimationList waitFor;
    private final ArrayList<MoveAnimation> animations;
    private boolean isCompleted;

    public AnimationList(AnimationList waitFor) {
        this.waitFor = waitFor;
        animations = new ArrayList<>();
        isCompleted = false;
    }

    public void addAnimation(MoveAnimation animation) {
        synchronized (animations) {
            animations.add(animation);
        }
    }

    @Override
    public synchronized void run() {
        if (waitFor != null)
            waitFor.waitToComplete();
        waitFor = null;
        synchronized (animations) {
            for (MoveAnimation animation : animations)
                if (animation != null)
                    animation.start();
            for (MoveAnimation animation : animations)
                if (animation != null)
                    animation.waitToComplete();
        }
        isCompleted = true;
        notifyAll();
    }

    public synchronized void waitToComplete() {
        try {
            while (!isCompleted)
                wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

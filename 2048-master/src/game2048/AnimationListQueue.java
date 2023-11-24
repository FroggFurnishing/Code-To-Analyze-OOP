package game2048;

/**
 * Project: game 2048
 * Created by Hadi on 6/2/14 1:36 PM.
 */
public class AnimationListQueue {
    private AnimationList current;

    public AnimationListQueue() {
        current = new AnimationList(null);
    }

    public void complete() {
        current.start();
        current = new AnimationList(current);
    }

    public void addAnimation(MoveAnimation animation) {
        current.addAnimation(animation);
    }
}

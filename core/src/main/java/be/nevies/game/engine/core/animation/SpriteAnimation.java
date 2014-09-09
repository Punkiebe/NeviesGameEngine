package be.nevies.game.engine.core.animation;

import be.nevies.game.engine.core.graphic.Sprite;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

/**
 * Basic class for sprite animations.
 *
 * @author drs
 */
public class SpriteAnimation extends Transition {

    private final Sprite sprite;

    private int lastIndex;

    private int animationNbrFrames;
    private int animationColumns;
    private double animationOffsetX;
    private double animationOffsetY;
    private double animationWidthFrame;
    private double animationHeightFrame;

    /**
     * Default constructor. With a duration of 1 second and a linear interpolator.
     *
     * @param sprite The sprite you want to animation.
     */
    public SpriteAnimation(Sprite sprite) {
        this.sprite = sprite;
        this.animationNbrFrames = sprite.getTotalNbrFrames();
        this.animationColumns = sprite.getColumns();
        this.animationOffsetX = sprite.getOffsetX();
        this.animationOffsetY = sprite.getOffsetY();
        this.animationHeightFrame = sprite.getHeightFrame();
        this.animationWidthFrame = sprite.getWidthFrame();
        setCycleDuration(Duration.seconds(1));
        setInterpolator(Interpolator.LINEAR);
    }

    /**
     * Default constructor with a linear interpolator.
     *
     * @param sprite The sprite you want to animation.
     * @param duration The duration of the animation.
     */
    public SpriteAnimation(Sprite sprite, Duration duration) {
        this.sprite = sprite;
        this.animationNbrFrames = sprite.getTotalNbrFrames();
        this.animationColumns = sprite.getColumns();
        this.animationOffsetX = sprite.getOffsetX();
        this.animationOffsetY = sprite.getOffsetY();
        this.animationHeightFrame = sprite.getHeightFrame();
        this.animationWidthFrame = sprite.getWidthFrame();
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    /**
     * Default constructor.
     *
     * @param sprite The sprite you want to animation.
     * @param duration The duration of the animation.
     * @param interpolator The interpolate type for the animation.
     */
    public SpriteAnimation(Sprite sprite, Duration duration, Interpolator interpolator) {
        this.sprite = sprite;
        this.animationNbrFrames = sprite.getTotalNbrFrames();
        this.animationColumns = sprite.getColumns();
        this.animationOffsetX = sprite.getOffsetX();
        this.animationOffsetY = sprite.getOffsetY();
        this.animationHeightFrame = sprite.getHeightFrame();
        this.animationWidthFrame = sprite.getWidthFrame();
        setCycleDuration(duration);
        setInterpolator(interpolator);
    }

    /**
     * While a Transition is running, this method is called in every frame.
     *
     * @param frac The relative position
     */
    @Override
    protected void interpolate(double frac) {
        final int index = Math.min((int) Math.floor(frac * animationNbrFrames), animationNbrFrames - 1);
        if (index != lastIndex) {
            final double x = (index % animationColumns) * animationWidthFrame + animationOffsetX;
            final double y = (index / animationColumns) * animationHeightFrame + animationOffsetY;
            sprite.getNode().setViewport(new Rectangle2D(x, y, animationWidthFrame, animationHeightFrame));
            lastIndex = index;
        }
    }

    public int getAnimationNbrFrames() {
        return animationNbrFrames;
    }

    public void setAnimationNbrFrames(int animationNbrFrames) {
        this.animationNbrFrames = animationNbrFrames;
    }

    public int getAnimationColumns() {
        return animationColumns;
    }

    public void setAnimationColumns(int animationColumns) {
        this.animationColumns = animationColumns;
    }

    public double getAnimationOffsetX() {
        return animationOffsetX;
    }

    public void setAnimationOffsetX(double animationOffsetX) {
        this.animationOffsetX = animationOffsetX;
    }

    public double getAnimationOffsetY() {
        return animationOffsetY;
    }

    public void setAnimationOffsetY(double animationOffsetY) {
        this.animationOffsetY = animationOffsetY;
    }

    public double getAnimationWidthFrame() {
        return animationWidthFrame;
    }

    public void setAnimationWidthFrame(double animationWidthFrame) {
        this.animationWidthFrame = animationWidthFrame;
    }

    public double getAnimationHeightFrame() {
        return animationHeightFrame;
    }

    public void setAnimationHeightFrame(double animationHeightFrame) {
        this.animationHeightFrame = animationHeightFrame;
    }

}

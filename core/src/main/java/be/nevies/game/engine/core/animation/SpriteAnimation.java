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

    /**
     * Default constructor.
     * 
     * @param sprite The sprite you want to animation.
     * @param duration The duration of the animation.
     * @param interpolator The interpolate type for the animation.
     */
    public SpriteAnimation(Sprite sprite, Duration duration, Interpolator interpolator) {
        this.sprite = sprite;
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
        final int index = Math.min((int) Math.floor(frac * sprite.getTotalNbrFrames()), sprite.getTotalNbrFrames() - 1);
        if (index != lastIndex) {
            final double x = (index % sprite.getColumns()) * sprite.getWidthFrame() + sprite.getOffsetX();
            final double y = (index / sprite.getColumns()) * sprite.getHeightFrame() + sprite.getOffsetY();
            sprite.getNode().setViewport(new Rectangle2D(x, y, sprite.getWidthFrame(), sprite.getHeightFrame()));
            lastIndex = index;
        }
    }
}

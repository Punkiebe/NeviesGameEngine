package be.nevies.game.engine.core.animation;

import be.nevies.game.engine.core.graphic.Sprite;
import javafx.animation.Interpolator;
import javafx.util.Builder;
import javafx.util.Duration;

/**
 * The builder for the class SpriteAnimation. The default values for this builder are :<br/> totalNbrFrames = 1, columns = 1, offsetX = 0, offsetY = 0,
 * widthOneFrame = 48, heightOneFrame = 48, duration = Duration.seconds(1) and interpolator = Interpolator.LINEAR
 *
 * @author drs
 */
public final class SpriteAnimationBuilder implements Builder<SpriteAnimation> {

    private Sprite sprite;
    private Duration duration;
    private Interpolator interpolator;

    /**
     * Hidden constructor so people use the create method.
     *
     * @param Sprite The sprite.
     */
    private SpriteAnimationBuilder(Sprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("The sprite can't be null!!");
        }
        this.sprite = sprite;
        this.duration = Duration.seconds(1);
        this.interpolator = Interpolator.LINEAR;
    }

    /**
     * Create the builder class, so you can start construct your SpriteAnimation.
     *
     * @param imageView The image view.
     * @return The builder object.
     */
    public static SpriteAnimationBuilder create(Sprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("The sprite can't be null!!");
        }
        return new SpriteAnimationBuilder(sprite);
    }

    /**
     * @return Create the SpriteAnimation object you have constructed.
     */
    @Override
    public SpriteAnimation build() {
        return new SpriteAnimation(sprite, duration, interpolator);
    }

    /**
     * @param totalNbrFrames The total number of frames.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder totalNbrFrames(int totalNbrFrames) {
        sprite.setTotalNbrFrames(totalNbrFrames);
        return this;
    }

    /**
     * @param columns The number of columns for the animation.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder columns(int columns) {
        sprite.setColumns(columns);
        return this;
    }

    /**
     * @param offsetX The offset for the x-axis.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder offsetX(double offsetX) {
        sprite.setOffsetX(offsetX);
        return this;
    }

    /**
     * @param offsetY The offset for the y-axis.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder offsetY(double offsetY) {
        sprite.setOffsetY(offsetY);
        return this;
    }

    /**
     * @param widthOneFrame The width of one frame.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder widthOneFrame(double widthOneFrame) {
        sprite.setWidthFrame(widthOneFrame);
        return this;
    }

    /**
     * @param heightOneFrame The height of one frame.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder heightOneFrame(double heightOneFrame) {
        sprite.setHeightFrame(heightOneFrame);
        return this;
    }

    /**
     * @param duration The Duration you want to set.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder duration(Duration duration) {
        this.duration = duration;
        return this;
    }

    /**
     * @param interpolator The interpolator method you want to set.
     * @return The builder it self.
     */
    public SpriteAnimationBuilder interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }
}

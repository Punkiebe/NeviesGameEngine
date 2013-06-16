package be.nevies.game.engine.core.graphic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Builder;

/**
 * This is the Builder class for the Sprite class.
 *
 * @author drs
 */
public class SpriteBuilder implements Builder<Sprite> {

    private ImageView imageView;
    private int totalNbrFrames = 1;
    private int columns = 1;
    private double offsetX = 0;
    private double offsetY = 0;
    private double widthFrame;
    private double heightFrame;

    /**
     * Default constructor. The height and width are default set to the height and width of the image, totalNbrFrames and columns to 1 and offsets to zero.
     *
     * @param imagePath The path to the image.
     */
    public SpriteBuilder(String imagePath) {
        if (imagePath == null || "".equals(imagePath.trim())) {
            throw new IllegalArgumentException("The image path can't be null!!");
        }
        this.imageView = new ImageView(imagePath);
        this.widthFrame = this.imageView.getImage().getWidth();
        this.heightFrame = this.imageView.getImage().getHeight();
    }

    /**
     * Constructor using an Image. The height and width are default set to the height and width of the image, totalNbrFrames and columns to 1 and offsets to
     * zero.
     *
     * @param imagePath The image.
     */
    public SpriteBuilder(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("The image can't be null!!");
        }
        this.imageView = new ImageView(image);
        this.widthFrame = this.imageView.getImage().getWidth();
        this.heightFrame = this.imageView.getImage().getHeight();
    }

    /**
     * Constructor using an ImageView object. The height and width are default set to the height and width of the image, totalNbrFrames and columns to 1 and
     * offsets to zero.
     *
     * @param imageView The ImageView object.
     */
    public SpriteBuilder(ImageView imageView) {
        if (imageView == null) {
            throw new IllegalArgumentException("The image view can't be null!!");
        }
        this.imageView = imageView;
        this.widthFrame = this.imageView.getImage().getWidth();
        this.heightFrame = this.imageView.getImage().getHeight();
    }

    /**
     * This will create the sprite object you put together.
     *
     * @return The sprite object you created with the builder.
     */
    @Override
    public Sprite build() {
        return new Sprite(imageView, totalNbrFrames, columns, offsetX, offsetY, widthFrame, heightFrame);
    }

    /**
     * @param totalNbrFrames The number of frames in this sprite image.
     * @return The builder it self.
     */
    public SpriteBuilder totalNbrFrames(int totalNbrFrames) {
        this.totalNbrFrames = totalNbrFrames;
        return this;
    }

    /**
     * @param columns The number of columns the sprite image has.
     * @return The builder it self.
     */
    public SpriteBuilder columns(int columns) {
        this.columns = columns;
        return this;
    }

    /**
     * @param offsetX The offset for the x-axis.
     * @return The builder it self.
     */
    public SpriteBuilder offsetX(double offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    /**
     * @param offsetY The offset for the y-axis.
     * @return The builder it self.
     */
    public SpriteBuilder offsetY(double offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    /**
     * @param widthFrame The width of one frame.
     * @return The builder it self.
     */
    public SpriteBuilder widthFrame(double widthFrame) {
        this.widthFrame = widthFrame;
        return this;
    }

    /**
     * @param heightFrame The height of one frame.
     * @return The builder it self.
     */
    public SpriteBuilder heightFrame(double heightFrame) {
        this.heightFrame = heightFrame;
        return this;
    }
}

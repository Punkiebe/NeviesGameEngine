package be.nevies.game.engine.core.graphic;

import be.nevies.game.engine.core.general.Element;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The Sprite class represents an element of the type ImageView.
 *
 * @author drs
 */
public class Sprite extends Element<ImageView> {

    private int totalNbrFrames;
    private int columns;
    private double offsetX;
    private double offsetY;
    private double widthFrame;
    private double heightFrame;

    /**
     * Default constructor.
     *
     * @param image The image of your sprite.
     */
    public Sprite(ImageView image) {
        super(image);
        this.totalNbrFrames = 1;
        this.columns = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.widthFrame = 1;
        this.heightFrame = 1;
    }

    /**
     * Complete constructor. This sets also the viewport of the image.
     *
     * @param image The image of your sprite.
     * @param totalNbrFrames The total of number of frames in your sprite.
     * @param columns The number of columns in your sprite.
     * @param offsetX The offset for the x-axe.
     * @param offsetY The offset for the y-axe.
     * @param widthFrame The width of one frame.
     * @param heightFrame The height of one frame.
     */
    public Sprite(ImageView image, int totalNbrFrames, int columns, double offsetX, double offsetY, double widthFrame, double heightFrame) {
        super(image);
        this.totalNbrFrames = totalNbrFrames;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.widthFrame = widthFrame;
        this.heightFrame = heightFrame;
        image.setViewport(new Rectangle2D(offsetX, offsetX, widthFrame, heightFrame));
    }

    /**
     * Complete constructor. This sets also the viewport of the image.
     *
     * @param image The image of your sprite.
     * @param offsetX The offset for the x-axe.
     * @param offsetY The offset for the y-axe.
     * @param widthFrame The width of one frame.
     * @param heightFrame The height of one frame.
     */
    public Sprite(ImageView image, double offsetX, double offsetY, double widthFrame, double heightFrame) {
        super(image);
        this.totalNbrFrames = 1;
        this.columns = 1;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.widthFrame = widthFrame;
        this.heightFrame = heightFrame;
        image.setViewport(new Rectangle2D(offsetX, offsetX, widthFrame, heightFrame));
    }

    /**
     * Constructor width default offsets at 0. This sets also the viewport of the image.
     *
     * @param image The image of your sprite.
     * @param totalNbrFrames The total of number of frames in your sprite.
     * @param columns The number of columns in your sprite.
     * @param widthFrame The width of one frame.
     * @param heightFrame The height of one frame.
     */
    public Sprite(int totalNbrFrames, int columns, double widthFrame, double heightFrame, ImageView image) {
        super(image);
        this.totalNbrFrames = totalNbrFrames;
        this.columns = columns;
        this.offsetX = 0;
        this.offsetY = 0;
        this.widthFrame = widthFrame;
        this.heightFrame = heightFrame;
        image.setViewport(new Rectangle2D(offsetX, offsetX, widthFrame, heightFrame));
    }

    /**
     * Constructor for an image with one frame. This sets also the viewport of the image.
     *
     * @param image The image of your sprite.
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public Sprite(ImageView image, double width, double height) {
        super(image);
        this.totalNbrFrames = 1;
        this.columns = 1;
        this.offsetX = 0;
        this.offsetY = 0;
        this.widthFrame = width;
        this.heightFrame = height;
        image.setViewport(new Rectangle2D(offsetX, offsetX, widthFrame, heightFrame));
    }

    /**
     * Complete constructor. This sets also the viewport of the image.
     *
     * @param image The image
     * @param viewport The viewport for this image.
     */
    public Sprite(Image image, Rectangle2D viewport) {
        super(new ImageView(image));
        this.totalNbrFrames = 1;
        this.columns = 1;
        this.offsetX = viewport.getMinX();
        this.offsetY = viewport.getMinY();
        this.widthFrame = viewport.getWidth();
        this.heightFrame = viewport.getHeight();
        getNode().setViewport(viewport);
    }

    public int getTotalNbrFrames() {
        return totalNbrFrames;
    }

    public int getColumns() {
        return columns;
    }

    public double getWidthFrame() {
        return widthFrame;
    }

    public double getHeightFrame() {
        return heightFrame;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setTotalNbrFrames(int totalNbrFrames) {
        this.totalNbrFrames = totalNbrFrames;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public void setWidthFrame(double widthFrame) {
        this.widthFrame = widthFrame;
    }

    public void setHeightFrame(double heightFrame) {
        this.heightFrame = heightFrame;
    }

    /**
     * Help method for easy calling of the imageView 'setX' method.<br> Same as :
     * <code> this.getNode().setX(x); </code>
     *
     * @param x
     */
    public void setX(double x) {
        this.getNode().setX(x);
    }

    /**
     * Help method for easy calling of the imageView 'setY' method.<br> Same as :
     * <code> this.getNode().setY(y); </code>
     *
     * @param y
     */
    public void setY(double y) {
        this.getNode().setY(y);
    }
}

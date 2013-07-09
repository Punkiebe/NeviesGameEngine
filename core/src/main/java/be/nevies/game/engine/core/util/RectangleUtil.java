/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.util;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

/**
 * This is an utility class for the Rectangle object.
 *
 * @author drs
 */
public final class RectangleUtil {

    /**
     * Private constructor.
     */
    private RectangleUtil() {
    }

    /**
     * @param rec The rectangle.
     * @return The Point2D coordinates of the center of the given rectangle.
     */
    public static Point2D centerPointOfRectangle(Rectangle rec) {
        return pointOnRectangle(rec, Direction.CENTER);
    }

    /**
     * @param rec The rectangle.
     * @param dir The direction.
     * @return The coordinates on the rectangle for the direction you asked.
     */
    public static Point2D pointOnRectangle(Rectangle rec, Direction dir) {
        if (rec == null) {
            throw new IllegalArgumentException("Rectangle can't be null.");
        }
        if (dir == null) {
            throw new IllegalArgumentException("Direction can't be null.");
        }
        double centerX;
        double centerY;
        switch (dir) {
            case CENTER:
                centerX = rec.getX() + (rec.getWidth() / 2);
                centerY = rec.getY() + (rec.getHeight() / 2);
                return new Point2D(centerX, centerY);
            case TOP:
                centerX = rec.getX() + (rec.getWidth() / 2);
                centerY = rec.getY();
                return new Point2D(centerX, centerY);
            case BOTTOM:
                centerX = rec.getX() + (rec.getWidth() / 2);
                centerY = rec.getY() + rec.getHeight();
                return new Point2D(centerX, centerY);
            case LEFT:
                centerX = rec.getX();
                centerY = rec.getY() + (rec.getHeight() / 2);
                return new Point2D(centerX, centerY);
            case RIGHT:
                centerX = rec.getX() + rec.getWidth();
                centerY = rec.getY() + (rec.getHeight() / 2);
                return new Point2D(centerX, centerY);
            case TOP_LEFT:
                centerX = rec.getX();
                centerY = rec.getY();
                return new Point2D(centerX, centerY);
            case TOP_RIGHT:
                centerX = rec.getX() + rec.getWidth();
                centerY = rec.getY();
                return new Point2D(centerX, centerY);
            case BOTTOM_RIGHT:
                centerX = rec.getX() + rec.getWidth();
                centerY = rec.getY() + rec.getHeight();
                return new Point2D(centerX, centerY);
            case BOTTOM_LEFT:
                centerX = rec.getX();
                centerY = rec.getY() + rec.getHeight();
                return new Point2D(centerX, centerY);
            default:
                throw new IllegalArgumentException("For the direction " + dir + " there's no point on the rectangle.");
        }
    }
}

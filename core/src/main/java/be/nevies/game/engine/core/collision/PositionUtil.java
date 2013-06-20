package be.nevies.game.engine.core.collision;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an utility class that helps to determine the position of two Bounds towards each other.
 *
 * @author drs
 */
public final class PositionUtil {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(PositionUtil.class);

    /**
     * Default constructor.
     */
    private PositionUtil() {
    }

    /**
     * @param pointOneLine First point of the line.
     * @param pointTwoLine Second point of the line.
     * @param pointToPosition Point you want to retrieve the position from.
     * @return The point position towards the line. If the line is horizontal, then left is above the line and right under.
     */
    public static PointPosition getPointPositionTowardsLine(Point2D pointOneLine, Point2D pointTwoLine, Point2D pointToPosition) {
        if (pointOneLine == null || pointTwoLine == null || pointToPosition == null) {
            throw new IllegalArgumentException("None of the points given can be null!!");
        }

        boolean swap = false;
        if (pointTwoLine.getX() - pointOneLine.getX() != 0) {
            double corner = Math.tan((pointOneLine.getY() - pointTwoLine.getY()) / (pointTwoLine.getX() - pointOneLine.getX()));
            if (corner > 0) {
                swap = true;
            }
        }
        double result = ((pointTwoLine.getX() - pointOneLine.getX()) * (pointToPosition.getY() - pointOneLine.getY()) - (pointTwoLine.getY() - pointOneLine.getY()) * (pointToPosition.getX() - pointOneLine.getX()));

        if (result > 0) {
            return swap ? PointPosition.RIGHT : PointPosition.LEFT;
        } else if (result < 0) {
            return swap ? PointPosition.LEFT : PointPosition.RIGHT;
        } else if (result == 0) {
            return PointPosition.ON;
        }

        throw new IllegalStateException("This can never happen!!");
    }

    /**
     * This method gives you the distance between two points.
     *
     * @param one First point.
     * @param two Second point.
     * @return The distance between the two points.
     */
    public static double getDistanceBetweenTwoPoints(Point2D one, Point2D two) {
        if (one == null || two == null) {
            throw new IllegalArgumentException("The give points can't be null to determin the distance between them.");
        }
        LOG.debug("Distance for point one : {} and two : {}", one, two);
        return Math.sqrt((one.getX() - two.getX()) * (one.getX() - two.getX()) + (one.getY() - two.getY()) * (one.getY() - two.getY()));
    }

    /**
     * This method gives you the middle point between two points.
     *
     * @param one First point.
     * @param Second point.
     * @return The middle point between two points.
     */
    public static Point2D getMiddlePointBetweenTwoPoints(Point2D one, Point2D two) {
        if (one == null || two == null) {
            throw new IllegalArgumentException("The give points can't be null to determin the middle point between them.");
        }
        return new Point2D((one.getX() + two.getX()) / 2, (one.getY() + two.getY()) / 2);
    }

    /**
     * This method gives you the direction from which side the source was hit by the target.
     *
     * @param source The source bounds.
     * @param target The target bounds.
     * @return The direction of the collision.
     */
    public static Direction getDirectionOfTwoCollidedElements(Bounds source, Bounds target) {
        double sX = source.getMinX();
        double sY = source.getMinY();
        double sW = source.getWidth();
        double sH = source.getHeight();
        double tX = target.getMinX();
        double tY = target.getMinY();
        double tW = target.getWidth();
        double tH = target.getHeight();

        printBoundInfo(source);
        printBoundInfo(target);

        Point2D firstLinePointOne = new Point2D(sX, sY);
        Point2D firstLinePointTwo = new Point2D(sX + sW, sY + sH);
        Point2D secondLinePointOne = new Point2D(sX, sY + sH);
        Point2D secondLinePointTwo = new Point2D(sX + sW, sY);

        Point2D pointToPosition = new Point2D((tX + (tW / 2)), (tY + (tH / 2)));
        PointPosition resultLineOne = getPointPositionTowardsLine(firstLinePointOne, firstLinePointTwo, pointToPosition);
        PointPosition resultLineTwo = getPointPositionTowardsLine(secondLinePointOne, secondLinePointTwo, pointToPosition);

        LOG.trace("Point position for first line : {} , for second line : {}.", resultLineOne, resultLineTwo);

        switch (resultLineOne.getCode() | (resultLineTwo.getCode() << 3)) {
            case 0b001_001:
                return Direction.LEFT;
            case 0b010_010:
                return Direction.RIGHT;
            case 0b001_010:
                return Direction.TOP;
            case 0b010_001:
                return Direction.BOTTOM;
            case 0b100_010:
                return Direction.TOP_RIGHT;
            case 0b010_100:
                return Direction.BOTTOM_RIGHT;
            case 0b100_001:
                return Direction.BOTTOM_LEFT;
            case 0b001_100:
                return Direction.TOP_LEFT;
            case 0b100_100:
                return Direction.CENTER;
            default:
                break;
        }

        return null;
    }

    /**
     * Show the info of a Bounds object in debug.
     *
     * @param bounds The bounds you want to print out.
     */
    private static void printBoundInfo(Bounds bounds) {
        StringBuilder buf = new StringBuilder();
        buf.append("Bounds[");
        buf.append("x : ").append(bounds.getMinX());
        buf.append(", y : ").append(bounds.getMinY());
        buf.append(", width : ").append(bounds.getWidth());
        buf.append(", height : ").append(bounds.getHeight());
        buf.append("]");
        LOG.debug(buf.toString());
    }
}

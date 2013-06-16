/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.collision;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for the util class CollisionPositionUtil.
 *
 * @author drs
 */
@RunWith(JUnit4.class)
public class CollisionPositionUtilTest {

    @Test
    public void getPointPositionTowardsLine() {
        try {
            CollisionPositionUtil.getPointPositionTowardsLine(null, null, null);
            fail();
        } catch (IllegalArgumentException iae) {
            // Oké
        }

        // Normal
        Point2D pointOneLine = new Point2D(0, 0);
        Point2D pointTwoLine = new Point2D(2, 2);
        Point2D pointToPosition = new Point2D(4, 0);
        PointPosition returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.RIGHT));

        pointOneLine = new Point2D(0, 0);
        pointTwoLine = new Point2D(2, 2);
        pointToPosition = new Point2D(-4, 0);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.LEFT));

        pointOneLine = new Point2D(0, 0);
        pointTwoLine = new Point2D(2, 2);
        pointToPosition = new Point2D(1, 1);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.ON));

        pointOneLine = new Point2D(0, 2);
        pointTwoLine = new Point2D(2, 0);
        pointToPosition = new Point2D(4, 0);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.RIGHT));

        pointOneLine = new Point2D(0, 2);
        pointTwoLine = new Point2D(2, 0);
        pointToPosition = new Point2D(-4, 0);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.LEFT));

        pointOneLine = new Point2D(0, 2);
        pointTwoLine = new Point2D(2, 0);
        pointToPosition = new Point2D(1, 1);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.ON));

        // Vertical
        pointOneLine = new Point2D(0, 0);
        pointTwoLine = new Point2D(0, 2);
        pointToPosition = new Point2D(1, 1);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.RIGHT));

        pointOneLine = new Point2D(0, 0);
        pointTwoLine = new Point2D(0, 2);
        pointToPosition = new Point2D(0, -100);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.ON));

        pointOneLine = new Point2D(0, 0);
        pointTwoLine = new Point2D(0, 2);
        pointToPosition = new Point2D(-1, -1);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.LEFT));

        // Horizontal
        pointOneLine = new Point2D(-2, 0);
        pointTwoLine = new Point2D(-5, 0);
        pointToPosition = new Point2D(5, 0);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.ON));

        pointOneLine = new Point2D(-2, 0);
        pointTwoLine = new Point2D(-5, 0);
        pointToPosition = new Point2D(5, 5);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.RIGHT));

        pointOneLine = new Point2D(-2, 0);
        pointTwoLine = new Point2D(-5, 0);
        pointToPosition = new Point2D(5, -5);
        returned = CollisionPositionUtil.getPointPositionTowardsLine(pointOneLine, pointTwoLine, pointToPosition);
        assertThat(returned, is(PointPosition.LEFT));
    }

    @Test
    public void getDirectionOfTwoCollidedElements() {
        Bounds source;
        Bounds target;
        Direction directionReturned;
        System.out.println("******* Check RIGHT *****************");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(10, 0, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.RIGHT));
        System.out.println("***********************************");
        System.out.println("******* Check TOP_RIGHT ***********");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(10, -10, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.TOP_RIGHT));
        System.out.println("***********************************");
        System.out.println("******* Check BOTTOM_RIGHT ********");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(10, 10, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.BOTTOM_RIGHT));
        System.out.println("***********************************");
        System.out.println("******* Check TOP *****************");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(0, -10, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.TOP));
        System.out.println("***********************************");
        System.out.println("******* Check BOTTOM **************");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(0, 10, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.BOTTOM));
        System.out.println("***********************************");
        System.out.println("******* Check TOP_LEFT ************");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(-10, -10, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.TOP_LEFT));
        System.out.println("***********************************");
        System.out.println("******* Check BOTTOM_LEFT *********");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(-10, 10, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.BOTTOM_LEFT));
        System.out.println("***********************************");
        System.out.println("******* Check LEFT ****************");
        source = new BoundingBox(0, 0, 10, 10);
        target = new BoundingBox(-10, 0, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.LEFT));
        System.out.println("***********************************");
        System.out.println("******* Check LEFT ****************");
        source = new BoundingBox(0, 0, 5, 5);
        target = new BoundingBox(-10, 0, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.LEFT));
        System.out.println("***********************************");
        System.out.println("******* Check TOP *****************");
        source = new BoundingBox(20, 10, 10, 10);
        target = new BoundingBox(25, 0, 10, 10);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.TOP));
        System.out.println("***********************************");
        System.out.println("******* Check BOTTOM **************");
        source = new BoundingBox(4, 25, 30, 15);
        target = new BoundingBox(0, 40, 32, 32);
        directionReturned = CollisionPositionUtil.getDirectionOfTwoCollidedElements(source, target);
        assertThat(directionReturned, is(Direction.BOTTOM));
        System.out.println("***********************************");
    }
}

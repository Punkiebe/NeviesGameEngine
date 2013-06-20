/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.collision;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test class for the RectangleUtil class.
 *
 * @author drs
 */
@RunWith(JUnit4.class)
public class RectangleUtilTest {
    
    @Test
    public void pointOnRectangle() {
        Rectangle rec = new Rectangle(10, 10);
        
        Point2D center = new Point2D(5, 5);
        Point2D point = RectangleUtil.pointOnRectangle(rec, Direction.CENTER);
        assertThat(center, is(point));
        center = new Point2D(0, 0);
        point = RectangleUtil.pointOnRectangle(rec, Direction.TOP_LEFT);
        assertThat(center, is(point));
        center = new Point2D(10, 0);
        point = RectangleUtil.pointOnRectangle(rec, Direction.TOP_RIGHT);
        assertThat(center, is(point));
        center = new Point2D(5, 0);
        point = RectangleUtil.pointOnRectangle(rec, Direction.TOP);
        assertThat(center, is(point));
        center = new Point2D(0, 5);
        point = RectangleUtil.pointOnRectangle(rec, Direction.LEFT);
        assertThat(center, is(point));
        center = new Point2D(0, 10);
        point = RectangleUtil.pointOnRectangle(rec, Direction.BOTTOM_LEFT);
        assertThat(center, is(point));
        center = new Point2D(10, 10);
        point = RectangleUtil.pointOnRectangle(rec, Direction.BOTTOM_RIGHT);
        assertThat(center, is(point));
        center = new Point2D(5, 10);
        point = RectangleUtil.pointOnRectangle(rec, Direction.BOTTOM);
        assertThat(center, is(point));
        center = new Point2D(10, 5);
        point = RectangleUtil.pointOnRectangle(rec, Direction.RIGHT);
        assertThat(center, is(point));
    }
}

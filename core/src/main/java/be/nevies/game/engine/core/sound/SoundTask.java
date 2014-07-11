/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.sound;

import be.nevies.game.engine.core.collision.CollisionUtil;
import be.nevies.game.engine.core.util.Direction;
import be.nevies.game.engine.core.util.PointPosition;
import be.nevies.game.engine.core.util.PositionUtil;
import be.nevies.game.engine.core.util.RectangleUtil;
import java.util.Collection;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class SoundTask extends Task<Void> {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SoundTask.class);

    private final Collection<Rectangle> mainElementBounds;
    private final Collection<SoundElement> soundElements;
    
    private long startTime;

    /**
     * Default constructor.
     *
     * @param mainElementBounds The bounds of the element that we're checking
     * for a collision and that's used to determine the volume and balance on.
     * @param soundElements The collection of all the sound elements that needs
     * to get there collision checked.
     */
    public SoundTask(Collection<Rectangle> mainElementBounds, Collection<SoundElement> soundElements) {
        this.mainElementBounds = mainElementBounds;
        this.soundElements = soundElements;
    }

    @Override
    protected Void call() throws Exception {
        startTime = System.nanoTime();
        for (SoundElement sound : soundElements) {
            if (!sound.hasSoundArea()) {
                // If there's no sound area no way to check for a collision.
                continue;
            }
            boolean intersects = false;
            Rectangle intersectRec = null;
            for (Rectangle bound : mainElementBounds) {
                intersects = bound.getBoundsInParent().intersects(sound.getSoundArea().getBoundsInParent());
                if (intersects) {
                    intersectRec = new Rectangle(bound.getBoundsInParent().getMinX(), bound.getBoundsInParent().getMinY(),
                            bound.getBoundsInParent().getWidth(), bound.getBoundsInParent().getHeight());
                    break;
                }
            }
            if (intersects && sound.isBalanceDirectionBased()) {
                // Change the balance base on the direction.
                Point2D areaOne = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.TOP);
                Point2D areaTwo = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.BOTTOM);
                Point2D areaCenter = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.CENTER);
                Point2D intersectPt = RectangleUtil.pointOnRectangle(intersectRec, Direction.CENTER);
                PointPosition position = PositionUtil.getPointPositionTowardsLine(areaOne, areaTwo, intersectPt);
                double distance;
                double percen;
                switch (position) {
                    case ON:
                        sound.setBalance(0.0);
                        break;
                    case LEFT:
                        distance = PositionUtil.getDistanceBetweenTwoPoints(areaCenter, intersectPt);
                        percen = calculatePercentageFromDistance(intersectRec, sound.getSoundArea(), distance);
                        sound.setBalance(1.0 - percen);
                        break;
                    case RIGHT:
                        distance = PositionUtil.getDistanceBetweenTwoPoints(areaCenter, intersectPt);
                        percen = calculatePercentageFromDistance(intersectRec, sound.getSoundArea(), distance);
                        sound.setBalance(-(1.0 - percen));
                        break;
                    default:
                        throw new AssertionError();
                }
                LOG.trace("The balance is now : {}", sound.getBalance());
            }
            if (intersects && sound.isVolumeDistanceBased()) {
                // Change the volume based on the distance.
                Point2D areaCenter = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.CENTER);
                Point2D intersectPt = RectangleUtil.pointOnRectangle(intersectRec, Direction.CENTER);
                double distance = PositionUtil.getDistanceBetweenTwoPoints(areaCenter, intersectPt);
                sound.setVolume(calculatePercentageFromDistance(intersectRec, sound.getSoundArea(), distance));
                LOG.trace("The volume is now : {}", sound.getVolume());
            }
            if (intersects) {
                if (sound.getStatus() != SoundElement.Status.PLAYING) {
                    sound.play();
                    LOG.debug("Start playing the sound : {}", sound.toString());
                }
            } else {
                if (sound.getStatus() == SoundElement.Status.PLAYING) {
                    sound.stop();
                    LOG.debug("Stop playing the sound : {}", sound.toString());
                }
            }
        }
        long total = System.nanoTime() - startTime;
        LOG.trace("SoundTask took : {}ns.", total);
        return null;
    }

    /**
     * This method only works if the two rectangles intersect each other.
     *
     * @param recOne First rectangle.
     * @param recTwo Second rectangle
     * @param distance The distance.
     * @return The percentage based on distance. 0.0 means there the furthest
     * away. 1.0 means there on top of each other.
     */
    private static double calculatePercentageFromDistance(Rectangle recOne, Rectangle recTwo, double distance) {
        if (recOne == null || recTwo == null) {
            throw new IllegalArgumentException("The rectangles can't be null!");
        }
        double max = CollisionUtil.maximumDistanceForIntersectingRectangles(recOne, recTwo);

        if (max == distance) {
            return 0.0;
        }
        double percent = (max - distance) / max;
        LOG.trace("Calculating percentage for distance. Max : {} , Distance : {} , Percentage : {}", max, distance, percent);
        return percent;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        LOG.trace("Sound collision task done.");
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        LOG.info("Sound collision task cancelled!");
    }

}

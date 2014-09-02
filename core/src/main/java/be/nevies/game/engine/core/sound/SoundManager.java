package be.nevies.game.engine.core.sound;

import be.nevies.game.engine.core.collision.CollisionUtil;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.sound.SoundElement.Status;
import be.nevies.game.engine.core.util.Direction;
import be.nevies.game.engine.core.util.PointPosition;
import be.nevies.game.engine.core.util.PositionUtil;
import be.nevies.game.engine.core.util.RectangleUtil;
import be.nevies.game.engine.core.util.SingleExecutorService;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public final class SoundManager {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SoundManager.class);
    private static SoundManager instance;
    private final Map<String, SoundElement> soundMap;
    private Element mainElement;
    private boolean checkingCollision = false;
    private final Group soundGroup;
    private final SingleExecutorService soundCheckService;

    /**
     * Default constructor.
     */
    private SoundManager() {
        soundMap = new HashMap<>();
        soundGroup = new Group();
        soundGroup.setId("SoundAreaLayer");
        soundCheckService = new SingleExecutorService();
    }

    /**
     * Before you can use the SoundManager you need to initialise the
     * SoundManager. This can be done by calling this method.
     *
     * @param mainGameNode Your main game node.
     */
    public static void initialise(Group mainGameNode) {
        if (mainGameNode == null) {
            throw new IllegalArgumentException("The main game node can't be null!");
        }
        if (instance != null) {
            LOG.error("The SoundManager is already initialised");
            return;
        }
        instance = new SoundManager();
        mainGameNode.getChildren().add(instance.soundGroup);
        LOG.info("Sound manager is initialised.");
    }

    /**
     * @return The single instance of this class.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            throw new IllegalAccessError("Please call first the static 'init' method before getting the first instance.");
        }
        return instance;
    }

    /**
     * @return True if the SoundManager is already initialised.
     */
    public static boolean isInitialised() {
        return instance != null;
    }

    /**
     * Add a SoundElement to the map of sounds.
     *
     * @param name The name of the sound element.
     * @param element The SoundElement object.
     */
    public static void addSoundElement(String name, SoundElement element) {
        if (name == null || element == null) {
            throw new IllegalArgumentException("Name and/or element can't be null when adding a SoundElement to the SoundManager!!");
        }
        if (getInstance().soundMap.containsKey(name)) {
            LOG.warn("For the name {} there is already a SoundElement in the map!", name);
            return;
        }
        LOG.info("Added the sound : '{}', containing the SoundElement : {}", name, element);
        getInstance().soundMap.put(name, element);
    }

    /**
     * This method we'll create of SoundElement using the AudioClip object and
     * add it to the map of sounds. The AudioClip type is best used for smaller
     * sound files, like sound effects.
     *
     * @param name The name of the sound element.
     * @param path The path of the AudioClip you want to add. It uses the
     * getReource of class.
     */
    public static void addSoundElementTypeAudioClip(String name, String path) {
        try {
            addSoundElement(name, new SoundElement(new AudioClip(SoundManager.class.getResource(path).toURI().toString())));
        } catch (URISyntaxException ex) {
            LOG.error("Problems creating the SoundElement with name '{}' and as path '{}'.", name, path);
        }
    }

    /**
     * This method we'll create of SoundElement using the Media object and add
     * it to the map of sounds. The Media type is best used for larger sound
     * files, like dialogs and music.
     *
     * @param name The name of the sound element.
     * @param path The path of the Media you want to add. It uses the getReource
     * of class.
     */
    public static void addSoundElementTypeMedia(String name, String path) {
        try {
            addSoundElement(name, new SoundElement(new Media(SoundManager.class.getResource(path).toURI().toString())));
        } catch (URISyntaxException ex) {
            LOG.error("Problems creating the SoundElement with name '{}' and as path '{}'.", name, path);
        }
    }

    /**
     * Get a SoundElement based on the name.
     *
     * @param name The name of the sound element.
     * @return The SoundElement or null if there's no sound element for the
     * name.
     */
    public static SoundElement getSoundElement(String name) {
        if (getInstance().soundMap.containsKey(name)) {
            return getInstance().soundMap.get(name);
        }
        return null;
    }

    /**
     * The main element is used to determen from where the sound is hearth from.
     * This need to be set if you want to use the distance based sound option.
     *
     * @param mainElement Your main element.
     */
    public static void setMainElement(Element mainElement) {
        getInstance().mainElement = mainElement;
    }

    /**
     * A static call to the checkForCollisions method.
     */
    public static void staticCheckForCollisions() {
        getInstance().checkForCollisions();
    }

    /**
     * This will check the main element against all the SoundElements that have
     * a sound area. If there's a collision with a SoundElement the sound well
     * be played. And if the 'volumeDistanceBased' is true then it we'll adapt
     * the volume based on the distance oft he main element to the center of the
     * sound area.
     */
    public void checkForCollisions() {
        if (getInstance().mainElement == null) {
            LOG.error("Can't check for collisions without a Main Element! Set the main element in the SoundManager.");
            return;
        }
        if (!getInstance().mainElement.hasCollisionBounds()) {
            LOG.error("The main element needs to have collision bounds! Add at least one collision bound to your element!");
            return;
        }
        getInstance().soundCheckService.submit(new SoundTask(getInstance().mainElement.getCollisionBounds(), getInstance().soundMap.values()));
    }

    /**
     * Stops collision checking.
     */
    public void stopCollisionCheck() {
        getInstance().soundCheckService.shutdown();
    }

//    /**
//     * Same as checkForCollisions but without threads.
//     */
//    private static void innerCheckForCollisions() {
//        if (checkingCollision) {
//            LOG.info("Already checking for collisions, this call we'll be ignored!!");
//            return;
//        }
//        checkingCollision = true;
//        try {
//            if (getInstance().mainElement == null) {
//                LOG.error("Can't check for collisions without a Main Element! Set the main element in the SoundManager.");
//                return;
//            }
//            if (!getInstance().mainElement.hasCollisionBounds()) {
//                LOG.error("The main element needs to have collision bounds! Add at least one collision bound to your element!");
//                return;
//            }
//            Collection<Rectangle> mainBounds = getInstance().mainElement.getCollisionBounds();
//            // check for collisions
//            Collection<SoundElement> values = getInstance().soundMap.values();
//            for (SoundElement sound : values) {
//                if (!sound.hasSoundArea()) {
//                    // If there's no sound area no way to check for a collision.
//                    continue;
//                }
//                boolean intersects = false;
//                Rectangle intersectRec = null;
//                for (Rectangle bound : mainBounds) {
//                    intersects = bound.getBoundsInParent().intersects(sound.getSoundArea().getBoundsInParent());
//                    if (intersects) {
//                        intersectRec = new Rectangle(bound.getBoundsInParent().getMinX(), bound.getBoundsInParent().getMinY(),
//                                bound.getBoundsInParent().getWidth(), bound.getBoundsInParent().getHeight());
//                        break;
//                    }
//                }
//                if (intersects && sound.isBalanceDirectionBased()) {
//                    // Change the balance base on the direction.
//                    Point2D areaOne = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.TOP);
//                    Point2D areaTwo = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.BOTTOM);
//                    Point2D areaCenter = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.CENTER);
//                    Point2D intersectPt = RectangleUtil.pointOnRectangle(intersectRec, Direction.CENTER);
//                    PointPosition position = PositionUtil.getPointPositionTowardsLine(areaOne, areaTwo, intersectPt);
//                    double distance;
//                    double percen;
//                    switch (position) {
//                        case ON:
//                            sound.setBalance(0.0);
//                            break;
//                        case LEFT:
//                            distance = PositionUtil.getDistanceBetweenTwoPoints(areaCenter, intersectPt);
//                            percen = calculatePercentageFromDistance(intersectRec, sound.getSoundArea(), distance);
//                            sound.setBalance(1.0 - percen);
//                            break;
//                        case RIGHT:
//                            distance = PositionUtil.getDistanceBetweenTwoPoints(areaCenter, intersectPt);
//                            percen = calculatePercentageFromDistance(intersectRec, sound.getSoundArea(), distance);
//                            sound.setBalance(-(1.0 - percen));
//                            break;
//                        default:
//                            throw new AssertionError();
//                    }
//                    LOG.trace("The balance is now : {}", sound.getBalance());
//                }
//                if (intersects && sound.isVolumeDistanceBased()) {
//                    // Change the volume based on the distance.
//                    Point2D areaCenter = RectangleUtil.pointOnRectangle(sound.getSoundArea(), Direction.CENTER);
//                    Point2D intersectPt = RectangleUtil.pointOnRectangle(intersectRec, Direction.CENTER);
//                    double distance = PositionUtil.getDistanceBetweenTwoPoints(areaCenter, intersectPt);
//                    sound.setVolume(calculatePercentageFromDistance(intersectRec, sound.getSoundArea(), distance));
//                    LOG.trace("The volume is now : {}", sound.getVolume());
//                }
//                if (intersects) {
//                    if (sound.getStatus() != Status.PLAYING) {
//                        sound.play();
//                        LOG.debug("Start playing the sound : {}", sound.toString());
//                    }
//                } else {
//                    if (sound.getStatus() == Status.PLAYING) {
//                        sound.stop();
//                        LOG.debug("Stop playing the sound : {}", sound.toString());
//                    }
//                }
//            }
//        } catch (RuntimeException rte) {
//            LOG.error("There where problems while checken for collisions.", rte);
//        } finally {
//            checkingCollision = false;
//        }
//    }

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

    /**
     * Add the sound element to the sound group. This is needed to make sure the
     * Element is know by the scene. This is only needed for SoundElement that
     * have a sound area.
     *
     * @param sound The sound element.
     */
    protected static void addSoundElementToSoundGroup(SoundElement sound) {
        getInstance().soundGroup.getChildren().add(sound.getSoundArea());
    }

    /**
     * Remove a sound element from the sound group.
     *
     * @param sound The sound element.
     */
    protected static void removeSoundElementFromSoundGroup(SoundElement sound) {
        getInstance().soundGroup.getChildren().remove(sound.getSoundArea());
    }

    /**
     * Inner class that creates an object of FutureTask.
     */
    private class CheckForCollisionsTask extends FutureTask<Void> {

        public CheckForCollisionsTask(final Collection<Rectangle> mainElementBounds, final Collection<SoundElement> soundElements) {
            super(new Runnable() {
                @Override
                public void run() {
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
                            if (sound.getStatus() != Status.PLAYING) {
                                sound.play();
                                LOG.debug("Start playing the sound : {}", sound.toString());
                            }
                        } else {
                            if (sound.getStatus() == Status.PLAYING) {
                                sound.stop();
                                LOG.debug("Stop playing the sound : {}", sound.toString());
                            }
                        }
                    }
                }
            }, null);
        }
    }
}

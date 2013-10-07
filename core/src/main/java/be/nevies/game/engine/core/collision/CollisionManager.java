package be.nevies.game.engine.core.collision;

import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.event.GameEventObject;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.util.Direction;
import be.nevies.game.engine.core.util.PositionUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CollisionManager handles all the collision checks. This collision manager works with two collections. One for active elements and an other for passive
 * elements. Active elements are checked against other active element and against passive element. Passive element are never checked against other passive
 * elements!
 *
 * @author drs
 */
public final class CollisionManager {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CollisionManager.class);
    private static CollisionManager instance;
    private List<Element> activeElements;
    private List<Element> passiveElements;
    private Map<Element, Collection<Rectangle>> passiveMap;
    private static boolean checkingCollision = false;
    private Map<Element, GameEventObject> resultMapLastCheck;

    /**
     * Default constructor.
     */
    private CollisionManager() {
        activeElements = new ArrayList<>();
        passiveElements = new ArrayList<>();
        passiveMap = new HashMap<>();
    }

    /**
     * @return The single instance of this class.
     */
    public static CollisionManager getInstance() {
        if (instance == null) {
            instance = new CollisionManager();
        }
        return instance;
    }

    /**
     * @param elements Add elements to the active collection.
     */
    public static void addActiveElement(Element... elements) {
        getInstance().activeElements.addAll(Arrays.asList(elements));
    }

    /**
     * @param element Remove an element of the active collection.
     * @return If the remove was successful or not.
     */
    public static boolean removeActiveElement(Element element) {
        return getInstance().activeElements.remove(element);
    }

    /**
     * @param elements Add elements to the passive collection.
     */
    public static void addPassiveElement(Element... elements) {
        getInstance().passiveElements.addAll(Arrays.asList(elements));
        for (Element element : elements) {
            getInstance().passiveMap.put(element, element.getCollisionBounds());
        }
    }

    /**
     * @param element Remove an element of the passive collection.
     * @return If the remove was successful or not.
     */
    public static boolean removePassiveElement(Element element) {
        return getInstance().passiveElements.remove(element);
    }

    /**
     * @param element Remove an element of the active or passive collection.
     * @return If the remove was successful or not.
     */
    public static boolean removeElement(Element element) {
        if (getInstance().activeElements.remove(element)) {
            return true;
        }
        return getInstance().passiveElements.remove(element);
    }

    /**
     * Checks for collisions between elements in the active collection against all other elements in the active collection and then it checks all active
     * elements against all elements in the passive collection. If a collision was found then there's a GameEvent fired of the type 'COLLISION_EVENT' from the
     * element that was the source from the check. If it was a check from active elements against passive elements then the source is always the active element.
     * The method keeps checking till all elements are visit.
     *
     * @TODO Rework the comments!!
     */
    public static void checkForCollisions() {
        if (checkingCollision) {
            LOG.info("Already checking for collisions, this call we'll be ignored!!");
            return;
        }
        checkingCollision = true;
        try {
            Map<Element, GameEventObject> resultMap = new HashMap<>();
            ArrayList<Element> activeIter = new ArrayList<>(getInstance().activeElements);
            for (Element checkActive : activeIter) {
                // First against also other active elements
                for (Element checkAgainst : activeIter) {
                    if (checkActive != checkAgainst) {
                        ReturnObjectCheckBounds collision = checkTwoCollectionsOfBounds(checkActive.getCollisionBounds(), checkAgainst.getCollisionBounds());
                        if (collision.isCollision()) {
                            GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());
                            checkActive.fireEvent(new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
                            checkAgainst.fireEvent(new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
                            resultMap.put(checkActive, gameEventObject);
                            resultMap.put(checkAgainst, gameEventObject);
                        }
                    }
                }
                // Then against passive elements
                ArrayList<Element> passiveIter = new ArrayList<>(getInstance().passiveElements);
                for (Element checkAgainst : passiveIter) {
                    if (checkActive != checkAgainst) {
                        ReturnObjectCheckBounds collision = checkTwoCollectionsOfBounds(checkActive.getCollisionBounds(), getInstance().passiveMap.get(checkAgainst));
                        if (collision.isCollision()) {
                            GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());
                            checkActive.fireEvent(new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
                            checkAgainst.fireEvent(new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
                            resultMap.put(checkActive, gameEventObject);
                            resultMap.put(checkAgainst, gameEventObject);
                        }
                    }
                }
            }
            getInstance().setResultMapLastCheck(resultMap);
        } catch (RuntimeException rte) {
            LOG.error("There where problems while checken for collisions.", rte);
        } finally {
            checkingCollision = false;
        }
    }

    /**
     * Checks for two elements there collection of bounds for collision.
     *
     * @param boundsOne First collection of bounds.
     * @param boundsTwo Second collection of bounds.
     * @return A ReturnOBjectCheckBouns object that holds information if it there was a collision, and if so the direction.
     */
    private static ReturnObjectCheckBounds checkTwoCollectionsOfBounds(Collection<Rectangle> boundsOne, Collection<Rectangle> boundsTwo) {
        for (Rectangle boundOne : boundsOne) {
            for (Rectangle boundTwo : boundsTwo) {
                boolean intersects = boundOne.getBoundsInParent().intersects(boundTwo.getBoundsInParent());
                if (intersects) {
                    Direction direction = PositionUtil.getDirectionOfTwoCollidedElements(boundTwo.getBoundsInParent(), boundOne.getBoundsInParent());
                    return ReturnObjectCheckBounds.collisionResponsTrue(direction);
                }
            }
        }
        return ReturnObjectCheckBounds.collisionResponsFalse();
    }

    /**
     * @return The total of active elements managed.
     */
    public int totalNumberOfActiveElements() {
        return activeElements.size();
    }

    /**
     * @return The total of passive elements managed.
     */
    public int totalNumberOfPassiveElements() {
        return passiveElements.size();
    }

    /**
     * @return The total of elements managed.
     */
    public int totalNumberOfElements() {
        return totalNumberOfActiveElements() + totalNumberOfPassiveElements();
    }

    /**
     * @return The result map of last check.
     */
    private Map<Element, GameEventObject> getResultMapLastCheck() {
        return resultMapLastCheck;
    }

    /**
     * @param resultMapLastCheck The result map of last check.
     */
    private void setResultMapLastCheck(Map<Element, GameEventObject> resultMapLastCheck) {
        this.resultMapLastCheck = resultMapLastCheck;
    }

    /**
     * Retrieve the game event object of last collision check for an element.
     *
     * @param element The element.
     * @return The game event object of the last collision, if there was a collision. Or null if there was in last check no collision for this element.
     */
    public static GameEventObject getGameEventForLastCollision(Element element) {
        return getInstance().getResultMapLastCheck().get(element);
    }

    /* Helper methods */
    /**
     * Prints out the current content of the Map 'resultMapLastCheck'. The content is printed out on the 'INFO' level.
     */
    public static void printContentResultMapLastCheck() {
        Map<Element, GameEventObject> map = getInstance().getResultMapLastCheck();
        LOG.info("------ ResultMapLastCheck ------");
        for (Element el : map.keySet()) {
            GameEventObject eventObj = map.get(el);
            LOG.info("The following element : {} has as last GameEventObject : {}.", el, eventObj);
        }
        LOG.info("--------------------------------");
    }

    /**
     * Prints out the current elements in the active elements list. The print out is done on the INFO level of the logger.
     */
    public static void printActiveElements() {
        LOG.info("--------- Active Elements ------");
        ArrayList<Element> activeIter = new ArrayList<>(getInstance().activeElements);
        for (Element el : activeIter) {
            LOG.info("{}", el);
        }
        LOG.info("--------------------------------");
    }

    /**
     * Prints out the current elements in the passive elements list. The print out is done on the INFO level of the logger.
     */
    public static void printPassiveElements() {
        LOG.info("--------- Passive Elements ------");
        ArrayList<Element> passiveIter = new ArrayList<>(getInstance().passiveElements);
        for (Element el : passiveIter) {
            LOG.info("{}", el);
        }
        LOG.info("--------------------------------");
    }
}

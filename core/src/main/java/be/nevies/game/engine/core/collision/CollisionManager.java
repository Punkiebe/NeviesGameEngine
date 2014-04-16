package be.nevies.game.engine.core.collision;

import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.event.GameEventObject;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.sound.SoundElement;
import be.nevies.game.engine.core.sound.SoundManager;
import static be.nevies.game.engine.core.sound.SoundManager.getInstance;
import be.nevies.game.engine.core.util.Direction;
import be.nevies.game.engine.core.util.PositionUtil;
import be.nevies.game.engine.core.util.SingleExecutorService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import javafx.concurrent.Task;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CollisionManager handles all the collision checks. This collision manager
 * works with two collections. One for active elements and an other for passive
 * elements. Active elements are checked against other active element and
 * against passive element. Passive element are never checked against other
 * passive elements!
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
    private static SingleExecutorService collisionCheckService;

    private static CollisionTask currentTask;

    /**
     * Default constructor.
     */
    private CollisionManager() {
        activeElements = new ArrayList<>();
        passiveElements = new ArrayList<>();
        passiveMap = new HashMap<>();
        collisionCheckService = new SingleExecutorService();
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
     * A static call to the checkForCollisions method.
     */
    public static void staticCheckForCollisions() {
        getInstance().checkForCollisions();
    }

    /**
     * Checks for collisions between elements in the active collection against
     * all other elements in the active collection and then it checks all active
     * elements against all elements in the passive collection. If a collision
     * was found then there are two GameEvents fired of the type
     * 'COLLISION_EVENT', one for the source and one for the target. With the
     * event there's also a GameEventObject. There the source is always the
     * active element, for active against active elements that's then the
     * current active element that's being checked.
     */
    public void checkForCollisions() {
        if (currentTask == null || currentTask.isCancelled()) {
            CollisionTask task = new CollisionTask(getInstance().activeElements, getInstance().passiveElements, getInstance().passiveMap);
            currentTask = task;
            Thread thread = new Thread(task, "Collision Check Thread");
            thread.setDaemon(false);
            thread.start();
        } else if (currentTask.isDone()) {
            try {
                getInstance().resultMapLastCheck = currentTask.get();
                CollisionTask task = new CollisionTask(getInstance().activeElements, getInstance().passiveElements, getInstance().passiveMap);
                currentTask = task;
                Thread thread = new Thread(task, "Collision Check Thread");
                thread.setDaemon(false);
                thread.start();
            } catch (InterruptedException | ExecutionException ex) {
                java.util.logging.Logger.getLogger(CollisionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            LOG.info("Current collision task still running, didn't start a new one.");
        }
    }

    /**
     * Stops collision checking.
     */
    public void stopCollisionCheck() {
        collisionCheckService.shutdown();
    }

//    /**
//     * Inner class that creates an object of FutureTask.
//     */
//    private class CheckForCollisionsTask extends FutureTask<Void> {
//
//        public CheckForCollisionsTask() {
//            super(new Runnable() {
//                @Override
//                public void run() {
//                    Map<Element, GameEventObject> resultMap = new HashMap<>();
//                    ArrayList<Element> activeIter = new ArrayList<>(getInstance().activeElements);
//                    for (Element checkActive : activeIter) {
//                        // First against also other active elements
//                        for (Element checkAgainst : activeIter) {
//                            if (checkActive != checkAgainst) {
//                                ReturnObjectCheckBounds collision = CollisionUtil.checkTwoCollectionsOfBounds(checkActive.getCollisionBounds(), checkAgainst.getCollisionBounds());
//                                if (collision.isCollision()) {
//                                    GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());
//                                    checkActive.fireEvent(new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
//                                    checkAgainst.fireEvent(new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
//                                    resultMap.put(checkActive, gameEventObject);
//                                    resultMap.put(checkAgainst, gameEventObject);
//                                }
//                            }
//                        }
//                        // Then against passive elements
//                        ArrayList<Element> passiveIter = new ArrayList<>(getInstance().passiveElements);
//                        for (Element checkAgainst : passiveIter) {
//                            if (checkActive != checkAgainst) {
//                                ReturnObjectCheckBounds collision = CollisionUtil.checkTwoCollectionsOfBounds(checkActive.getCollisionBounds(), getInstance().passiveMap.get(checkAgainst));
//                                if (collision.isCollision()) {
//                                    GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());
//                                    checkActive.fireEvent(new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
//                                    checkAgainst.fireEvent(new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
//                                    resultMap.put(checkActive, gameEventObject);
//                                    resultMap.put(checkAgainst, gameEventObject);
//                                }
//                            }
//                        }
//                    }
//                    getInstance().setResultMapLastCheck(resultMap);
//                }
//            }, null);
//        }
//    }
//    public Task<Map<Element, GameEventObject>> getNewCollisionTask() {
//        Task<Map<Element, GameEventObject>> task = new Task<Map<Element, GameEventObject>>() {
//
//            @Override
//            protected Map<Element, GameEventObject> call() throws Exception {
//                Map<Element, GameEventObject> resultMap = new HashMap<>();
//                ArrayList<Element> activeIter = new ArrayList<>(getInstance().activeElements);
//                for (Element checkActive : activeIter) {
//                    // First against also other active elements
//                    ArrayList<Element> activeIterTarget = new ArrayList<>(getInstance().activeElements);
//                    for (Element checkAgainst : activeIterTarget) {
//                        if (checkActive != checkAgainst) {
//                            ReturnObjectCheckBounds collision = CollisionUtil.checkTwoCollectionsOfBounds(checkActive.getCollisionBounds(), checkAgainst.getCollisionBounds());
//                            if (collision.isCollision()) {
//                                GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());
//                                checkActive.fireEvent(new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
//                                checkAgainst.fireEvent(new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
//                                resultMap.put(checkActive, gameEventObject);
//                                resultMap.put(checkAgainst, gameEventObject);
//                            }
//                        }
//                    }
//                    // Then against passive elements
//                    ArrayList<Element> passiveIter = new ArrayList<>(getInstance().passiveElements);
//                    for (Element checkAgainst : passiveIter) {
//                        if (checkActive != checkAgainst) {
//                            ReturnObjectCheckBounds collision = CollisionUtil.checkTwoCollectionsOfBounds(checkActive.getCollisionBounds(), getInstance().passiveMap.get(checkAgainst));
//                            if (collision.isCollision()) {
//                                GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());
//                                checkActive.fireEvent(new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
//                                checkAgainst.fireEvent(new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
//                                resultMap.put(checkActive, gameEventObject);
//                                resultMap.put(checkAgainst, gameEventObject);
//                            }
//                        }
//                    }
//                }
//                return resultMap;
//            }
//
//        };
//        return task;
//    }
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
     * @return The game event object of the last collision, if there was a
     * collision. Or null if there was in last check no collision for this
     * element.
     */
    public static GameEventObject getGameEventForLastCollision(Element element) {
        return getInstance().getResultMapLastCheck().get(element);
    }

    /* Helper methods */
    /**
     * Prints out the current content of the Map 'resultMapLastCheck'. The
     * content is printed out on the 'INFO' level.
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
     * Prints out the current elements in the active elements list. The print
     * out is done on the INFO level of the logger.
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
     * Prints out the current elements in the passive elements list. The print
     * out is done on the INFO level of the logger.
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

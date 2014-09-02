/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.collision;

import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.event.GameEventObject;
import be.nevies.game.engine.core.general.Element;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.Task;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task that checks for collisions between elements in the active collection
 * against all other elements in the active collection and then it checks all
 * active elements against all elements in the passive collection. If a
 * collision was found then there are two GameEvents fired of the type
 * 'COLLISION_EVENT', one for the source and one for the target. With the event
 * there's also a GameEventObject. There the source is always the active
 * element, for active against active elements that's then the current active
 * element that's being checked. These events are fired after all the collision
 * are checked and the call method is done.
 *
 * @author drs
 */
public class CollisionTask extends Task<Void> {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CollisionTask.class);

    private final List<Element> activeElements;

    private final List<Element> passiveElements;

    private final Map<Element, Collection<Rectangle>> passiveMap;

    private final Map<Element, List<GameEvent>> fireMap;
    
    private final Map<Element, Collection<GameEventObject>> resultReturn;

    /**
     * Constructor of the CollisionTask object.
     * 
     * @param active List of active elements.
     * @param passive List of passive elements.
     * @param passiveBoundsMap The bounds map of the passive elements.
     */
    public CollisionTask(List<Element> active, List<Element> passive, Map<Element, Collection<Rectangle>> passiveBoundsMap) {
        activeElements = active;
        passiveElements = passive;
        passiveMap = passiveBoundsMap;
        fireMap = new HashMap<>();
        resultReturn = new HashMap<>();
    }

    @Override
    protected Void call() throws Exception {
        LOG.trace("Start checking for collisions (task).");
        long nanoTimeStart = System.nanoTime();
       // Map<Element, GameEventObject> result = new HashMap<>();
        ArrayList<Element> activeIter = new ArrayList<>(activeElements);
        int totalActive = activeIter.size();
        int count = 0;
        updateProgress(count, totalActive);
        for (Element checkActive : activeIter) {
            // Bounds of the current active element
            Collection<Rectangle> activeBounds = checkActive.getCollisionBounds();
            // First against also other active elements
            ArrayList<Element> activeIterTarget = new ArrayList<>(activeElements);
            for (Element checkAgainst : activeIterTarget) {
                if (checkActive != checkAgainst) {
                    ReturnObjectCheckBounds collision = CollisionUtil.checkTwoCollectionsOfBounds(activeBounds, checkAgainst.getCollisionBounds());
                    if (collision.isCollision()) {
                        GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());

                        addEventToFire(checkActive, new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
                        addEventToFire(checkAgainst, new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
                        addGameEventObjectToResult(checkActive, gameEventObject);
                        addGameEventObjectToResult(checkAgainst, gameEventObject);
                    }
                }
            }
            // Then against passive elements
            ArrayList<Element> passiveIter = new ArrayList<>(passiveElements);
            for (Element checkAgainst : passiveIter) {
                if (checkActive != checkAgainst) {
                    ReturnObjectCheckBounds collision = CollisionUtil.checkTwoCollectionsOfBounds(activeBounds, passiveMap.get(checkAgainst));
                    if (collision.isCollision()) {
                        GameEventObject gameEventObject = new GameEventObject(checkActive, checkAgainst, collision.getDirection());
                        addEventToFire(checkActive, new GameEvent(gameEventObject, checkActive, GameEvent.COLLISION_EVENT));
                        addEventToFire(checkAgainst, new GameEvent(gameEventObject, checkAgainst, GameEvent.COLLISION_EVENT));
                        addGameEventObjectToResult(checkActive, gameEventObject);
                        addGameEventObjectToResult(checkAgainst, gameEventObject);
                    }
                }
            }
            count++;
            updateProgress(count, totalActive);
            if (isCancelled()) {
                break;
            }
        }
        LocalTime endTime = LocalTime.ofNanoOfDay(System.nanoTime() - nanoTimeStart);
        LOG.debug("CollisionTask took : {}ns ({}ms) ({})", endTime.format(DateTimeFormatter.ofPattern("N")), endTime.format(DateTimeFormatter.ofPattern("A")), this);
        return null;
    }

    /**
     * Adds a GameEvent to the map to fire the events of later.
     * 
     * @param element The element.
     * @param event The GameEvent.
     */
    private void addEventToFire(Element element, GameEvent event) {
        if (!fireMap.containsKey(element)) {
            fireMap.put(element, new ArrayList<>());
        }
        fireMap.get(element).add(event);
    }

    /**
     * Adds a GameEventObject to the map 'resultReturn'.
     * 
     * @param element The element
     * @param event The GameEventObject.
     */
    private void addGameEventObjectToResult(Element element, GameEventObject event) {
        if (!resultReturn.containsKey(element)) {
            resultReturn.put(element, new ArrayList<>());
        }
        resultReturn.get(element).add(event);
    }

    @Override
    protected synchronized void succeeded() {
        super.succeeded();
        //synchronized (this) {
            CollisionManager.getInstance().getResultMapLastCheck().clear();
            CollisionManager.getInstance().getResultMapLastCheck().putAll(resultReturn);
        //}
        LOG.debug("Collision task done. Fire events. Number of events to fire : {}", fireMap.size());
       // fireMap.keySet().forEach(key -> fireMap.get(key).forEach(event -> key.fireEvent(event)));
        
        
        Set<Element> keySet = fireMap.keySet();
        for (Element element : keySet) {
            List<GameEvent> events = fireMap.get(element);
            for (GameEvent gameEvent : events) {
                element.fireEvent(gameEvent);
            }
        }
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        LOG.info("Collision task cancelled!");
    }

}

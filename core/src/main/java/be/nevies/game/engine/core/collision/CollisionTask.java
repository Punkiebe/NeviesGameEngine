/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.collision;

import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.event.GameEventObject;
import be.nevies.game.engine.core.general.Element;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class CollisionTask extends Task<Map<Element, GameEventObject>> {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CollisionTask.class);

    private final List<Element> activeElements;

    private final List<Element> passiveElements;

    private final Map<Element, Collection<Rectangle>> passiveMap;

    private final Map<Element, List<GameEvent>> fireMap;

    public CollisionTask(List<Element> active, List<Element> passive, Map<Element, Collection<Rectangle>> passiveBoundsMap) {
        activeElements = active;
        passiveElements = passive;
        passiveMap = passiveBoundsMap;
        fireMap = new HashMap<>();
    }

    @Override
    protected Map<Element, GameEventObject> call() throws Exception {
        LOG.trace("Start checking for collisions.");
        Map<Element, GameEventObject> resultMap = new HashMap<>();
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
                        resultMap.put(checkActive, gameEventObject);
                        resultMap.put(checkAgainst, gameEventObject);
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
                        resultMap.put(checkActive, gameEventObject);
                        resultMap.put(checkAgainst, gameEventObject);
                    }
                }
            }
            count++;
            updateProgress(count, totalActive);
            if (isCancelled()) {
                break;
            }
        }
        return resultMap;
    }

    private void addEventToFire(Element element, GameEvent event) {
        if (!fireMap.containsKey(element)) {
            fireMap.put(element, new ArrayList<>());
        }
        fireMap.get(element).add(event);
    }

    @Override
    protected void succeeded() {
        super.succeeded();
       // LOG.debug("Collision task done. Fire events. Number of events to fire : {}", fireMap.size());
        fireMap.keySet().forEach(key -> fireMap.get(key).forEach(event -> key.fireEvent(event)));
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        LOG.info("Collision task cancelled!");
    }

}

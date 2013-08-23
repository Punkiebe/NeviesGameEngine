/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.util;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.event.GameEventObject;
import be.nevies.game.engine.core.general.BehaviourType;
import be.nevies.game.engine.core.general.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an utility class for Collision related methods.
 * 
 * @author drs
 */
public class CollisionUtil {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(CollisionUtil.class);

    /**
     * Checks if an element didn't have any collisions with other element that have BehaviourType.NOT_CROSSABLE for the direction given.
     *
     * @param element The element you want to check.
     * @param directionToMoveIn The direction you want to move in to.
     * @return True if it's OK to move, else false.
     */
    public static boolean checkBeforeMove(Element element, Direction directionToMoveIn) {
        if (element == null) {
            throw new IllegalArgumentException("The element can't be null.");
        }
        if (directionToMoveIn == null) {
            throw new IllegalArgumentException("The directionToMoveIn can't be null.");
        }
        GameEventObject gameEvent = CollisionManager.getGameEventForLastCollision(element);
        if (gameEvent == null) {
            return true;
        }
        if (gameEvent.getSource() == element) {
            if (gameEvent.getTarget().getBehaviourTypes().contains(BehaviourType.NOT_CROSSABLE)) {
                switch (directionToMoveIn) {
                    case LEFT:
                        return gameEvent.getDirection() != Direction.RIGHT ? true : false;
                    case RIGHT:
                        return gameEvent.getDirection() != Direction.LEFT ? true : false;
                    case TOP:
                        return gameEvent.getDirection() != Direction.BOTTOM ? true : false;
                    case BOTTOM:
                        return gameEvent.getDirection() != Direction.TOP ? true : false;
                    default:
                        return true;
                }
            }
        } else if (gameEvent.getTarget() == element) {
            if (gameEvent.getSource().getBehaviourTypes().contains(BehaviourType.NOT_CROSSABLE)) {
                switch (directionToMoveIn) {
                    case LEFT:
                        return gameEvent.getDirection() != Direction.LEFT ? true : false;
                    case RIGHT:
                        return gameEvent.getDirection() != Direction.RIGHT ? true : false;
                    case TOP:
                        return gameEvent.getDirection() != Direction.TOP ? true : false;
                    case BOTTOM:
                        return gameEvent.getDirection() != Direction.BOTTOM ? true : false;
                    default:
                        return true;
                }
            }
        } else {
            LOG.error("You should never be able to reach here. Element : {} , Direction : {} .", element, directionToMoveIn);
        }
        return true;
    }
}

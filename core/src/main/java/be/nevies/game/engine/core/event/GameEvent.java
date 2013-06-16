package be.nevies.game.engine.core.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * This is the common GameEvent class for you to extend from. So you can create your own custom GameEvents.
 *
 * @author drs
 */
public class GameEvent extends Event {

    /**
     * This is a common supertype event for all the GameEvents.
     */
    public static final EventType<GameEvent> ANY = new EventType<>("GameEvent_ANY");
    /**
     * This event is given when a collision happens between two elements. Check for this event type when you want to handle a collision for that element.
     */
    public static final EventType<GameEvent> COLLISION_EVENT = new EventType<>(ANY, "GameEvent_COLLISION");
    /**
     * This is event is given each time the game update time line is called.
     */
    public static final EventType<GameEvent> GAME_UPDATE_EVENT = new EventType<>(ANY, "GameEvent_GAME_UPDATE");
    /* This is the object that hold some extra info for when a GameEvent event is fired. */
    private GameEventObject gameEventObject;

    /**
     * Default constructor that calls the super constructor.
     *
     * @param type The EventType you want to fire a GameEvent for.
     */
    public GameEvent(EventType<? extends Event> type) {
        super(type);
    }

    /**
     *
     * @param obj The GameEventObject for when you want to give some more info about the event.
     * @param target The Element that's the cause of this event.
     * @param type The EventType you want to fire a GameEvent for.
     */
    public GameEvent(GameEventObject obj, EventTarget target, EventType<? extends Event> type) {
        super(target, target, type);
        this.gameEventObject = obj;
    }

    /**
     * @return GameEventObject for this GameEvent.
     */
    public GameEventObject getGameEventObject() {
        return gameEventObject;
    }
}

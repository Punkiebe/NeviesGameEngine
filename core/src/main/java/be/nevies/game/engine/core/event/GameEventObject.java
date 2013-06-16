package be.nevies.game.engine.core.event;

import be.nevies.game.engine.core.collision.Direction;
import be.nevies.game.engine.core.general.Element;

/**
 * This is an object so you can give extra information about the GameEvent thats been fired. You even can set here also an extra object of your choosing. The
 * source, target or direction can all be null. This is only an info object. Nothing here is used by the JavaFX framework it self.
 *
 * @author drs
 */
public class GameEventObject {

    private Element source;
    private Element target;
    private Object extra;
    private Direction direction;

    /**
     * Default constructor. To give the source and target of the GameEvent.
     *
     * @param source The source of this event.
     * @param target The target of this event.
     */
    public GameEventObject(Element source, Element target) {
        this.source = source;
        this.target = target;
    }

    /**
     * Constructor mainly used for the collision event.
     *
     * @param source The source of this event.
     * @param target The target of this event.
     * @param direction The direction of the collision between the source and target.
     */
    public GameEventObject(Element source, Element target, Direction direction) {
        this.source = source;
        this.target = target;
        this.direction = direction;
    }

    /**
     * @return The source of this event.
     */
    public Element getSource() {
        return source;
    }

    /**
     * @return The target from this event.
     */
    public Element getTarget() {
        return target;
    }

    /**
     * @return The direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return The extra object.
     */
    public Object getExtra() {
        return extra;
    }

    /**
     * @param extra Set any object you want here.
     */
    public void setExtra(Object extra) {
        this.extra = extra;
    }
}

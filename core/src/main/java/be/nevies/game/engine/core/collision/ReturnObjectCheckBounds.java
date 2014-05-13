package be.nevies.game.engine.core.collision;

import be.nevies.game.engine.core.util.Direction;

/**
 * This object is used to return the result of a collision check from the CollisionManager.
 *
 * @author drs
 */
public final class ReturnObjectCheckBounds {

    private Direction direction;
    private final boolean collision;

    /**
     * Default constructor for when a collision happened.
     *
     * @param direction The direction of the collision.
     * @param collision Collision happened or not.
     */
    private ReturnObjectCheckBounds(Direction direction, boolean collision) {
        this.direction = direction;
        this.collision = collision;
    }

    /**
     * Default constructor for when no collision happened.
     *
     * @param collision Collision happened or not.
     */
    private ReturnObjectCheckBounds(boolean collision) {
        this.collision = collision;
    }

    /**
     * @return The direction in which the collision happened seen from the point of view of the source.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @return If a collision happened or not.
     */
    public boolean isCollision() {
        return collision;
    }

    /**
     * @return The ReturnObjectCheckBounds object with a negative result.
     */
    protected static ReturnObjectCheckBounds collisionResponsFalse() {
        return new ReturnObjectCheckBounds(false);
    }

    /**
     * @param direction Direction from the collision, seen from the point of view of the source.
     * @return The ReturnObjectCheckBounds object with a positive result.
     */
    protected static ReturnObjectCheckBounds collisionResponsTrue(Direction direction) {
        return new ReturnObjectCheckBounds(direction, true);
    }
}

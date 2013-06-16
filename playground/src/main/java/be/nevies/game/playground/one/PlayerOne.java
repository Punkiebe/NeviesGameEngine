/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.collision.Direction;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.event.GameEventObject;
import be.nevies.game.engine.core.general.BehaviourType;
import be.nevies.game.engine.core.general.Element;
import java.util.Collection;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 *
 * @author drs
 */
public class PlayerOne extends Element<Node> {

    private boolean moveable = true;
    private Direction lastCollisionDirection;

    public PlayerOne() {
        super(new Circle(15));
        Bounds bounds = new BoundingBox(-15, -15, 30, 30);
        addCollisionBounds(bounds);
       // bounds = new BoundingBox(-7.5, -15, 15, 30);
       // addCollisionBounds(bounds);
        showCollisionBounds();
    }

    public void moveUp() {
        CollisionManager.checkForCollisions();
        System.out.println(">> last collision direction : " + lastCollisionDirection);
        if (lastCollisionDirection != Direction.BOTTOM || moveable) {
            moveY(-1);
        }
        lastCollisionDirection = null;
        moveable = true;
    }

    public void moveDown() {
        CollisionManager.checkForCollisions();
        System.out.println(">> last collision direction : " + lastCollisionDirection);
        if (lastCollisionDirection != Direction.TOP || moveable) {
            moveY(+1);
        }
        lastCollisionDirection = null;
        moveable = true;
    }

    public void moveRight() {
        CollisionManager.checkForCollisions();
        System.out.println(">> last collision direction : " + lastCollisionDirection);
        if (lastCollisionDirection != Direction.LEFT || moveable) {
            moveX(+1);
        }
        lastCollisionDirection = null;
        moveable = true;
        // getNode().setTranslateX(getNode().getTranslateX() + 1);
    }

    public void moveLeft() {
        CollisionManager.checkForCollisions();
        System.out.println(">> last collision direction : " + lastCollisionDirection);
        if (lastCollisionDirection != Direction.RIGHT || moveable) {
            moveX(-1);
        }
        lastCollisionDirection = null;
        moveable = true;
        // getNode().setTranslateX(getNode().getTranslateX() - 1);
    }

    @Override
    protected void handleCollision(GameEvent event) {
        super.handleCollision(event);
        System.out.println("Player handles collision " + event.getSource() + " " + event.getTarget());
        System.out.println("source : " + event.getGameEventObject().getSource());
        System.out.println("target : " + event.getGameEventObject().getTarget());
        System.out.println("behaviour : " + event.getGameEventObject().getTarget().getBehaviourTypes());
        System.out.println("current object : " + this);

        if (event.getGameEventObject().getTarget().hasBehaviourTypes()) {
            handleBehaviourTypes(event.getGameEventObject().getTarget().getBehaviourTypes(), event.getGameEventObject());
        }
    }

    private void handleBehaviourTypes(Collection<String> behaviourTypes, GameEventObject gameEventObject) {
        for (String behaviourType : behaviourTypes) {
            switch (behaviourType) {
                case PlayGroundOneBehaviour.NOT_CROSSABLE:
                    System.out.println("Stop moving");
                    lastCollisionDirection = gameEventObject.getDirection();
                    moveable = false;
                    break;
                case PlayGroundOneBehaviour.OWN_BEHAVIOUR: 
                    System.out.println("Own behaviour");
                    lastCollisionDirection = gameEventObject.getDirection();
                    break;
                default:
                    continue;
            }
        }
    }
}

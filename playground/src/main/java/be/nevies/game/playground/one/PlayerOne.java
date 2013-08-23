/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.util.Direction;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.event.GameEventObject;
import be.nevies.game.engine.core.general.BehaviourType;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.util.CollisionUtil;
import java.util.Collection;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author drs
 */
public class PlayerOne extends Element<Node> {

    public PlayerOne() {
        super(new Circle(15));
        Bounds bounds = new BoundingBox(-15, -15, 30, 30);
        addCollisionBounds(bounds);
        // bounds = new BoundingBox(-7.5, -15, 15, 30);
        // addCollisionBounds(bounds);
        showCollisionBounds();
    }

    public void moveUp() {
        if (CollisionUtil.checkBeforeMove(this, Direction.TOP)) {
            moveY(-1);
        }
    }

    public void moveDown() {
        if (CollisionUtil.checkBeforeMove(this, Direction.BOTTOM)) {
            moveY(+1);
        }
    }

    public void moveRight() {
        if (CollisionUtil.checkBeforeMove(this, Direction.RIGHT)) {
            moveX(+1);
        }
    }

    public void moveLeft() {
        if (CollisionUtil.checkBeforeMove(this, Direction.LEFT)) {
            moveX(-1);
        }
    }

    public Bullet shootBullet(Group gameGroup) {
        Rectangle bulletRec = new Rectangle(getLayoutX(), getLayoutY(), 5, 3);
        Bullet bullet = new Bullet(bulletRec, gameGroup);
        bullet.addCollisionBounds(bullet.getBoundsInLocal());
        bullet.addBehaviour(PlayGroundOneBehaviour.BULLET_BEHAVIOUR);
        bullet.showCollisionBounds();
        return bullet;
    }

    @Override
    protected void handleCollision(GameEvent event) {
        super.handleCollision(event);
//        System.out.println("Player handles collision " + event.getSource() + " " + event.getTarget());
//        System.out.println("source : " + event.getGameEventObject().getSource());
//        System.out.println("target : " + event.getGameEventObject().getTarget());
//        System.out.println("behaviour : " + event.getGameEventObject().getTarget().getBehaviourTypes());
//        System.out.println("current object : " + this);

        if (event.getGameEventObject().getTarget().hasBehaviourTypes()) {
            handleBehaviourTypes(event.getGameEventObject().getTarget().getBehaviourTypes(), event.getGameEventObject());
        }
    }

    private void handleBehaviourTypes(Collection<String> behaviourTypes, GameEventObject gameEventObject) {
        for (String behaviourType : behaviourTypes) {
            switch (behaviourType) {
                case PlayGroundOneBehaviour.NOT_CROSSABLE:
                    System.out.println("Stop moving");
                    break;
                case PlayGroundOneBehaviour.OWN_BEHAVIOUR:
                    System.out.println("Own behaviour");
                    break;
                default:
                    continue;
            }
        }
    }
    
}

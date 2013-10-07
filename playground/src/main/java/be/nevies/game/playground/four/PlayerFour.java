/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.four;

import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.util.CollisionUtil;
import be.nevies.game.engine.core.util.Direction;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 *
 * @author drs
 */
public class PlayerFour extends Element<Node> {

    public PlayerFour() {
        super(new Circle(15));
        Bounds bounds = new BoundingBox(-15, -15, 30, 30);
        addCollisionBounds(bounds);
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
}

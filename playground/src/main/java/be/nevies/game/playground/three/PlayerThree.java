/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.three;

import be.nevies.game.engine.core.general.Element;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 *
 * @author drs
 */
public class PlayerThree extends Element<Node> {

    public PlayerThree() {
        super(new Circle(15));
        Bounds bounds = new BoundingBox(-15, -15, 30, 30);
        addCollisionBounds(bounds);
        showCollisionBounds();
    }

    public void moveUp() {
        moveY(-1);
    }

    public void moveDown() {
        moveY(+1);
    }

    public void moveRight() {
        moveX(+1);
    }

    public void moveLeft() {
        moveX(-1);
    }
}

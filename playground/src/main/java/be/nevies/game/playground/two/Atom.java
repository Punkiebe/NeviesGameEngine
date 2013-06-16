/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.two;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.general.Element;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;

/**
 *
 * @author drs
 */
public class Atom extends Element<Circle> {

    /**
     * velocity vector x direction
     */
    public double vX = 0;
    /**
     * velocity vector y direction
     */
    public double vY = 0;

    public Atom(double radius, Color fill, Group group) {
        super(CircleBuilder.create()
                .centerX(radius)
                .centerY(radius)
                .radius(radius)
                .cache(true)
                .build(), group);

        RadialGradient rgrad = RadialGradientBuilder.create()
                .centerX(this.getNode().getCenterX() - this.getNode().getRadius() / 3)
                .centerY(this.getNode().getCenterY() - this.getNode().getRadius() / 3)
                .radius(this.getNode().getRadius())
                .proportional(false)
                .stops(new Stop(0.0, fill), new Stop(1.0, Color.BLACK))
                .build();

        this.getNode().setFill(rgrad);

        this.addEventHandler(GameEvent.COLLISION_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                System.out.println(">> The atom got hit by something remove it." + t.getGameEventObject().getSource() + " - " + t.getGameEventObject().getTarget() + " - " + t.getGameEventObject().getDirection());
                CollisionManager.removeActiveElement(t.getGameEventObject().getSource());
                t.getGameEventObject().getSource().removeElementFromGroup();
            }
        });
    }

    /**
     * Change the velocity of the atom particle.
     */
    public void update() {
        moveX(vX);
        moveY(vY);
    }
}

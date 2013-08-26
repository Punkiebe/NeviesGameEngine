/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.general.Element;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author drs
 */
public class Bullet extends Element<Rectangle> {

    public Bullet(Rectangle node, Group group) {
        super(node, group);
        
        this.addCollisionBounds(node.getBoundsInLocal());
        
        getNode().getScene().getRoot().addEventHandler(GameEvent.GAME_UPDATE_EVENT, new EventHandler<GameEvent>() {

            @Override
            public void handle(GameEvent t) {
                moveX(1);
            }
            
        });
        
         CollisionManager.addActiveElement(this);
    }

    @Override
    protected void handleCollision(GameEvent event) {
        super.handleCollision(event);
        
//        System.out.println("Bullet handles collision " + event.getSource() + " " + event.getTarget());
//        System.out.println("source : " + event.getGameEventObject().getSource());
//        System.out.println("target : " + event.getGameEventObject().getTarget());
//        System.out.println("behaviour : " + event.getGameEventObject().getTarget().getBehaviourTypes());
//        System.out.println("current object : " + this);
    }
    
    
    
    
}

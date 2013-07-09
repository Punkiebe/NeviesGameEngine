/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.two;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.general.BehaviourType;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.general.GameController;
import java.util.Random;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author drs
 */
public class PlayGroundTwo extends GameController {

    private final static Label NUM_SPRITES_FIELD = new Label();

    public PlayGroundTwo(Stage stage, int gups, int sups, String title, double widthWindow, double heightWindow) {
        super(stage, gups, sups, title, widthWindow, heightWindow);
    }

    @Override
    public void initialise() {
        final Timeline gameLoop = getGameUpdatesTimeline();
        
        VBox stats = VBoxBuilder.create()
                .spacing(5)
                .translateX(10)
                .translateY(10)
                .children(HBoxBuilder.create()
                .spacing(5)
                .children(new Label("Number of Particles: "), // show no. particles
                NUM_SPRITES_FIELD).build(),
                // button to build more spheres
                ButtonBuilder.create()
                .text("Regenerate")
                .onMousePressed(new EventHandler() {
            @Override
            public void handle(Event arg0) {
                generateManySpheres(10);
            }
        }).build(),
                // button to freeze game loop
                ButtonBuilder.create()
                .text("Freeze/Resume")
                .onMousePressed(new EventHandler() {
            @Override
            public void handle(Event arg0) {
                switch (gameLoop.getStatus()) {
                    case RUNNING:
                        gameLoop.stop();
                        break;
                    case STOPPED:
                        gameLoop.play();
                        break;
                }
            }
        }).build()).build(); // (VBox) stats on children
        MenuBox menu = new MenuBox(stats);
        
        // lay down the controls
        addElementToGameMainNode(menu);

        getGameScene().addEventFilter(GameEvent.GAME_UPDATE_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                System.out.println("Filter game event : " + t.toString());
                handleMoveAtoms();
                CollisionManager.checkForCollisions();
            }
        });



        generateManySpheres(10);
    }

    @Override
    protected void handleGameUpdate() {
        super.handleGameUpdate();
        System.out.println(">> handle game update method");
        //handleMoveAtoms();
    }

    /**
     * Make some more space spheres (Atomic particles)
     */
    private void generateManySpheres(int numSpheres) {
        Random rnd = new Random();
        Scene gameSurface = getGameScene();
        for (int i = 0; i < numSpheres; i++) {
            Color c = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            int rad = rnd.nextInt(15) + 5;
            Atom b = new Atom(rad, c, getGameMainNode());
            Circle circle = b.getNode();
            // random 0 to 2 + (.0 to 1) * random (1 or -1)
            b.vX = (rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);
            b.vY = (rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);

            // random x between 0 to width of scene
            double newX = rnd.nextInt((int) gameSurface.getWidth());

            // check for the right of the width newX is greater than width 
            // minus radius times 2(width of sprite)
            if (newX > (gameSurface.getWidth() - (circle.getRadius() * 2))) {
                newX = gameSurface.getWidth() - (circle.getRadius() * 2);
            }

            // check for the bottom of screen the height newY is greater than height
            // minus radius times 2(height of sprite)
            double newY = rnd.nextInt((int) gameSurface.getHeight());
            if (newY > (gameSurface.getHeight() - (circle.getRadius() * 2))) {
                newY = gameSurface.getHeight() - (circle.getRadius() * 2);
            }

            circle.setLayoutX(newX);
            circle.setLayoutY(newY);
            circle.setVisible(true);
            circle.setId(b.toString());

            // Collision info
            b.addBehaviour(BehaviourType.NOT_CROSSABLE);
            b.addCollisionBounds(circle.getBoundsInParent());
            b.showCollisionBounds();
            CollisionManager.addActiveElement(b);
        }
    }

    private void handleMoveAtoms() {
        ObservableList<Node> children = getGameMainNode().getChildren();

        for (Node node : children) {

            if (node instanceof Atom) {
                Atom sphere = (Atom) node;

                // advance the spheres velocity
                sphere.update();

                // bounce off the walls when outside of boundaries
                if (sphere.getBoundsInParent().getMinX() > (getGameScene().getWidth()
                        - sphere.getBoundsInParent().getWidth())
                        || sphere.getBoundsInParent().getMinX() < 0) {
                    sphere.vX = sphere.vX * -1;
                }
                if (sphere.getBoundsInParent().getMinY() > getGameScene().getHeight()
                        - sphere.getBoundsInParent().getHeight()
                        || sphere.getBoundsInParent().getMinY() < 0) {
                    sphere.vY = sphere.vY * -1;
                }
            }
        }
    }
}

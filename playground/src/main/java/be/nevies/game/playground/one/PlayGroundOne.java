/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.animation.PathElement;
import be.nevies.game.engine.core.animation.SpriteAnimation;
import be.nevies.game.engine.core.animation.SpriteAnimationBuilder;
import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.graphic.Sprite;
import javafx.animation.PathTransition;
import javafx.animation.PathTransitionBuilder;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author drs
 */
public class PlayGroundOne extends GameController {

    PlayerOne player = new PlayerOne();

    public PlayGroundOne(Stage stage, int gups, int sups, String title, double widthWindow, double heightWindow) {
        super(stage, gups, sups, title, widthWindow, heightWindow);
    }

    @Override
    public void initialise() {
        setupInput(getGameScene());

        FreeTileSetVersion9Mapper tileSet = new FreeTileSetVersion9Mapper();
        Sprite sprite1 = tileSet.getSprite(FreeTileSetVersion9Mapper.TILE_GROUND_TYPE_1);
        sprite1.setX(40);
        sprite1.setY(30);
        Sprite sprite2 = tileSet.getSprite(FreeTileSetVersion9Mapper.TILE_GROUND_TYPE_1);
        sprite2.setX(120);
        sprite2.setY(30);
        Sprite sprite3 = tileSet.getSprite(FreeTileSetVersion9Mapper.TILE_GROUND_TYPE_3);
        sprite3.setX(64);
        sprite3.setY(100);
        Sprite sprite4 = tileSet.getSprite(FreeTileSetVersion9Mapper.TILE_GROUND_TYPE_1);
        sprite4.setX(96);
        sprite4.setY(100);
        addElementToGameMainNode(sprite1);
        addElementToGameMainNode(sprite2);
        addElementToGameMainNode(sprite3);
        addElementToGameMainNode(sprite4);

        sprite1.addCollisionBounds(sprite1.getBoundsInLocal());
        sprite1.showCollisionBounds();
        sprite1.addBehaviour(PlayGroundOneBehaviour.NOT_CROSSABLE);
        sprite2.addCollisionBounds(sprite2.getBoundsInLocal());
        sprite2.showCollisionBounds();
        sprite2.addBehaviour(PlayGroundOneBehaviour.OWN_BEHAVIOUR);
        sprite3.addCollisionBounds(sprite3.getBoundsInLocal());
        sprite3.showCollisionBounds();
        sprite4.addCollisionBounds(sprite4.getBoundsInLocal());
        sprite4.showCollisionBounds();
        CollisionManager.addPassiveElement(sprite1, sprite2, sprite3, sprite4);

        ImageView imageView = new ImageView("be/nevies/game/playground/one/images/george_0.png");
        Sprite spriteGeorge = new Sprite(16, 4, 48, 48, imageView);
        SpriteAnimation build = SpriteAnimationBuilder.create(spriteGeorge).columns(1).totalNbrFrames(4).duration(Duration.seconds(5)).build();
        build.play();
        
        
        Path path = PathBuilder.create()
                .elements(
                    new MoveTo(50,50),
                    new CubicCurveTo(380, 0, 380, 120, 200, 120),
                    new CubicCurveTo(0, 120, 0, 240, 380, 240)
                )
                .build();
        path.setStroke(Color.DODGERBLUE);
        path.getStrokeDashArray().setAll(5d,5d);
        
        PathElement pathEl = new PathElement(new Path(new MoveTo(100, 200)), getGameMainNode());
        
        //pathEl.addPathElement(new LineTo(100, 200));
        
       // pathEl.addPathElement(new MoveTo(100, 200));
        pathEl.setLayoutX(100);
        pathEl.setLayoutY(50);
        
        pathEl.getNode().setStroke(Color.BLUE);
        pathEl.getNode().getStrokeDashArray().setAll(5d,5d);
        pathEl.getNode().autosize();
        pathEl.autosize();
        
        PathTransition pathTransition = PathTransitionBuilder.create()
                .duration(Duration.seconds(4))
                .path(pathEl.getNode())
                .node(spriteGeorge)
                .orientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        
        pathTransition.play();
        

//        getGameScene().addEventFilter(GameEvent.ANY, new EventHandler<GameEvent>() {
//            @Override
//            public void handle(GameEvent t) {
//                System.out.println("Filter any game event : " + t.toString());
//            }
//        });

        getGameScene().addEventFilter(GameEvent.COLLISION_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                System.out.println("Filter collision event : " + t.toString());
            }
        });

        CollisionManager.addActiveElement(player);

        addElementToGameMainNode(spriteGeorge);
        addElementToGameMainNode(player);
    }

    private void setupInput(Scene scene) {
        EventHandler movePlayerEvent = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                switch (t.getCode()) {
                    case UP:
                        player.moveUp();
                        break;
                    case DOWN:
                        player.moveDown();
                        break;
                    case LEFT:
                        player.moveLeft();
                        break;
                    case RIGHT:
                        player.moveRight();
                        break;
                }
            }
        };
        scene.setOnKeyPressed(movePlayerEvent);
    }
}

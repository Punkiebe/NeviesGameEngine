/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.animation.SpriteAnimation;
import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.collision.CollisionUtil;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.graphic.Sprite;
import be.nevies.game.engine.core.util.Direction;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class PlayGroundOne extends GameController {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(PlayGroundOne.class);

    PlayerOne player = new PlayerOne();

    public PlayGroundOne(int gups, int sups, String title) {
        super(gups, sups, title);
    }

    @Override
    public void initialise() {
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
        final Sprite spriteGeorge = new Sprite(16, 4, 48, 48, imageView);

        SpriteAnimation leftWalkAnim = new SpriteAnimation(spriteGeorge);
        leftWalkAnim.setAnimationColumns(1);
        leftWalkAnim.setAnimationNbrFrames(4);
        leftWalkAnim.setAnimationOffsetX(48);
        leftWalkAnim.setCycleCount(Timeline.INDEFINITE);
        SpriteAnimation rightWalkAnim = new SpriteAnimation(spriteGeorge);
        rightWalkAnim.setAnimationColumns(1);
        rightWalkAnim.setAnimationNbrFrames(4);
        rightWalkAnim.setAnimationOffsetX(144);
        rightWalkAnim.setCycleCount(Timeline.INDEFINITE);
        SpriteAnimation upWalkAnim = new SpriteAnimation(spriteGeorge);
        upWalkAnim.setAnimationColumns(1);
        upWalkAnim.setAnimationNbrFrames(4);
        upWalkAnim.setAnimationOffsetX(96);
        upWalkAnim.setCycleCount(Timeline.INDEFINITE);
        SpriteAnimation downWalkAnim = new SpriteAnimation(spriteGeorge);
        downWalkAnim.setAnimationColumns(1);
        downWalkAnim.setAnimationNbrFrames(4);
        downWalkAnim.setAnimationOffsetX(0);
        downWalkAnim.setCycleCount(Timeline.INDEFINITE);

        player.setLayoutX(90);
        player.setLayoutY(40);
//        player.addBehaviour(PlayGroundOneBehaviour.NOT_CROSSABLE);

        spriteGeorge.addBehaviour(PlayGroundOneBehaviour.NOT_CROSSABLE);
        spriteGeorge.addCollisionBounds(spriteGeorge.getBoundsInLocal());
        spriteGeorge.showCollisionBounds();

        CollisionManager.addActiveElement(spriteGeorge);

        Path leftPath = new Path();
        leftPath.getElements().add(new MoveTo(400, 200));
        leftPath.getElements().add(new LineTo(200, 200));
        leftPath.setStroke(Color.BLUE);
        leftPath.getStrokeDashArray().addAll(5d, 5d);
        // Only needed if you want to show the path
        getGameMainNode().getChildren().add(leftPath);
        
        PathTransition leftWalkPath = new PathTransition(Duration.seconds(10), leftPath);
        
        ParallelTransition leftWalkGroup = new ParallelTransition(spriteGeorge);
        leftWalkGroup.getChildren().add(leftWalkAnim);
        leftWalkGroup.getChildren().add(leftWalkPath);
        leftWalkGroup.play();
        
//        PathElement pathEl = new PathElement(new Path(new MoveTo(400, 200)), getGameMainNode());
//        pathEl.getNode().setStroke(Color.BLUE);
//        pathEl.getNode().getStrokeDashArray().setAll(5d, 5d);

//        final PathTransition pathTransition = PathTransitionBuilder.create()
//                .duration(Duration.seconds(10))
//                .path(path)
//                .node(spriteGeorge)
//                .orientation(PathTransition.OrientationType.NONE)
//                .cycleCount(3)
//                .autoReverse(true)
//                .interpolator(Interpolator.LINEAR)
//                .build();
//        pathTransition.currentRateProperty().addListener(new ChangeListener<Number>() {
//
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                leftWalk.stop();
//                rightWalk.stop();
//                System.out.println(">>>> changed rate : " + oldValue + " -> " + newValue);
//                if (newValue.equals(1.0)) {
//                    leftWalk.play();
//                } else if (newValue.equals(-1.0)) {
//                    rightWalk.play();
//                } else {
//                    leftWalk.stop();
//                    rightWalk.stop();
//                }
//            }
//        });
//        pathTransition.play();

        spriteGeorge.addEventHandler(GameEvent.COLLISION_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                LOG.debug("Handle collision event sprite george : {}.", t);
                handleGeorge(leftWalkGroup, Direction.LEFT, spriteGeorge);
            }
        });

        sprite2.addEventHandler(GameEvent.COLLISION_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                LOG.debug(">>Handle bullet");
                Element source = t.getGameEventObject().getSource();
                Element target = t.getGameEventObject().getTarget();
                if (source.checkForBehaviour(PlayGroundOneBehaviour.BULLET_BEHAVIOUR)) {
//                    boolean removeElementFromGroup = target.removeElementFromGroup();
//                    System.out.println(">> removed : " + removeElementFromGroup);
                    target.removeElement();
                    CollisionManager.removeElement(target);
                }
            }
        });

        CollisionManager.addActiveElement(player);

        addElementToGameMainNode(spriteGeorge);
        addElementToGameMainNode(player);
    }

    private static void handleGeorge(final Transition path, final Direction direction, Sprite george) {
        if (!CollisionUtil.checkBeforeMove(george, direction)) {
            path.stop();
        }
    }

    @Override
    public void defineSceneEvents(Scene scene) {
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
                    case SPACE:
                        player.shootBullet(getGameMainNode());
//                        CollisionManager.printActiveElements();
//                        CollisionManager.printPassiveElements();
//                        CollisionManager.printContentResultMapLastCheck();
                        break;
                }
            }
        };
        scene.setOnKeyPressed(movePlayerEvent);

//        getGameScene().addEventFilter(GameEvent.ANY, new EventHandler<GameEvent>() {
//            @Override
//            public void handle(GameEvent t) {
//                System.out.println("Filter any game event : " + t.toString());
//            }
//        });
//        getGameScene().addEventFilter(GameEvent.COLLISION_EVENT, new EventHandler<GameEvent>() {
//            @Override
//            public void handle(GameEvent t) {
//                System.out.println("Filter collision event : " + t.toString());
//            }
//        });
//        scene.addEventHandler(GameEvent.GAME_UPDATE_EVENT, new EventHandler<GameEvent>() {
//            @Override
//            public void handle(GameEvent t) {
//               // LOG.debug("Game update event : start collision check");
//                // System.out.println("Game update event check collision : " + t.toString());
//                CollisionManager.staticCheckForCollisions();
//            }
//        });
    }

    @Override
    protected void handleGameUpdate() {
        super.handleGameUpdate(); //To change body of generated methods, choose Tools | Templates.
        CollisionManager.staticCheckForCollisions();
    }

}

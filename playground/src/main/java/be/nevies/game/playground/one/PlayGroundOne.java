/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.animation.PathElement;
import be.nevies.game.engine.core.animation.SpriteAnimation;
import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.graphic.Sprite;
import be.nevies.game.engine.core.collision.CollisionUtil;
import be.nevies.game.engine.core.util.Direction;
import javafx.animation.PathTransition;
import javafx.animation.PathTransitionBuilder;
import javafx.animation.Timeline;
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

        SpriteAnimation build1 = new SpriteAnimation(spriteGeorge);
        build1.setAnimationColumns(1);
        build1.setAnimationNbrFrames(4);
        build1.setAnimationOffsetX(48);
        build1.setCycleCount(Timeline.INDEFINITE);
        build1.play();

        player.setLayoutX(90);
        player.setLayoutY(40);
//        player.addBehaviour(PlayGroundOneBehaviour.NOT_CROSSABLE);

        spriteGeorge.addBehaviour(PlayGroundOneBehaviour.NOT_CROSSABLE);
        spriteGeorge.addCollisionBounds(spriteGeorge.getBoundsInLocal());
        spriteGeorge.showCollisionBounds();

        CollisionManager.addActiveElement(spriteGeorge);

        PathElement pathEl = new PathElement(new Path(new MoveTo(400, 200)), getGameMainNode());
        pathEl.addPathElement(new LineTo(200, 200));
        pathEl.getNode().setStroke(Color.BLUE);
        pathEl.getNode().getStrokeDashArray().setAll(5d, 5d);

        final PathTransition pathTransition = PathTransitionBuilder.create()
                .duration(Duration.seconds(10))
                .path(pathEl.getNode())
                .node(spriteGeorge)
                .orientation(PathTransition.OrientationType.NONE)
                .cycleCount(1)
                .autoReverse(true)
                .build();
        pathTransition.play();

        spriteGeorge.addEventHandler(GameEvent.COLLISION_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                LOG.debug("Handle collision event sprite george : {}.", t);
                handleGeorge(pathTransition, Direction.LEFT, spriteGeorge);
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

    private static void handleGeorge(final PathTransition path, final Direction direction, Sprite george) {
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

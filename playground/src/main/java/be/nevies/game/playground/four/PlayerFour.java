/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.four;

import be.nevies.game.engine.core.animation.SpriteAnimation;
import be.nevies.game.engine.core.collision.CollisionUtil;
import be.nevies.game.engine.core.general.BehaviourType;
import be.nevies.game.engine.core.graphic.Sprite;
import be.nevies.game.engine.core.util.Direction;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author drs
 */
public class PlayerFour extends Sprite {

    private SpriteAnimation leftAnimation;
    private SpriteAnimation rightAnimation;
    private SpriteAnimation downAnimation;
    private SpriteAnimation upAnimation;

    public PlayerFour() {
        super(16, 4, 48, 48, new ImageView("be/nevies/game/playground/four/images/george.png"));
        this.addBehaviour(BehaviourType.NOT_CROSSABLE);
        this.addCollisionBounds(this.getBoundsInLocal());
        this.showCollisionBounds();

        leftAnimation = new SpriteAnimation(this);
        leftAnimation.setAnimationColumns(1);
        leftAnimation.setAnimationNbrFrames(4);
        leftAnimation.setAnimationOffsetX(48);
        leftAnimation.setCycleCount(Timeline.INDEFINITE);
        rightAnimation = new SpriteAnimation(this);
        rightAnimation.setAnimationColumns(1);
        rightAnimation.setAnimationNbrFrames(4);
        rightAnimation.setAnimationOffsetX(144);
        rightAnimation.setCycleCount(Timeline.INDEFINITE);
        downAnimation = new SpriteAnimation(this);
        downAnimation.setAnimationColumns(1);
        downAnimation.setAnimationNbrFrames(4);
        downAnimation.setAnimationOffsetX(0);
        downAnimation.setCycleCount(Timeline.INDEFINITE);
        upAnimation = new SpriteAnimation(this);
        upAnimation.setAnimationColumns(1);
        upAnimation.setAnimationNbrFrames(4);
        upAnimation.setAnimationOffsetX(96);
        upAnimation.setCycleCount(Timeline.INDEFINITE);
    }

    public void stopAllAnimation() {
        leftAnimation.stop();
        rightAnimation.stop();
        downAnimation.stop();
        upAnimation.stop();
    }

    public void moveUp() {
        if (CollisionUtil.checkBeforeMove(this, Direction.TOP)) {
            moveY(-1);
            leftAnimation.stop();
            rightAnimation.stop();
            downAnimation.stop();
            upAnimation.play();
        }
    }

    public void moveDown() {
        if (CollisionUtil.checkBeforeMove(this, Direction.BOTTOM)) {
            moveY(+1);
            leftAnimation.stop();
            rightAnimation.stop();
            upAnimation.stop();
            downAnimation.play();
        }
    }

    public void moveRight() {
        if (CollisionUtil.checkBeforeMove(this, Direction.RIGHT)) {
            moveX(+1);
            leftAnimation.stop();
            downAnimation.stop();
            upAnimation.stop();
            rightAnimation.play();
        }
    }

    public void moveLeft() {
        if (CollisionUtil.checkBeforeMove(this, Direction.LEFT)) {
            moveX(-1);
            rightAnimation.stop();
            downAnimation.stop();
            upAnimation.stop();
            leftAnimation.play();
        }
    }
}

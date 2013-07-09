/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.three;

import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.sound.SoundElement;
import be.nevies.game.engine.core.sound.SoundManager;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class PlayGroundThree extends GameController {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SoundManager.class);
    public static final String PIANO_MUSIC = "piano_music";
    public static final String PIANO_MUSIC_TWO = "piano_music_two";
    public static final String PIANO_MUSIC_THREE = "piano_music_three";
    public static final String PIANO_MUSIC_FOUR = "piano_music_four";
    public static final String CLOCK_SOUND = "clock_sound";
    public static final String BUTTON_CLICK_SOUND = "button_click_sound";
    public static final String STATIC_NOISE_TV_SOUND = "static_noise_tv_sound";
    private PlayerThree player = new PlayerThree();

    public PlayGroundThree(Stage stage, int gups, int sups, String title, double widthWindow, double heightWindow) {
        super(stage, gups, sups, title, widthWindow, heightWindow);
    }

    @Override
    public void initialise() {
        LOG.info("Start loading sound");
        SoundManager.initialise(getGameMainNode());
        SoundManager.addSoundElementTypeMedia(PIANO_MUSIC, "/be/nevies/game/playground/three/sound/classical_piano_music_track_mary_.mp3");
        SoundManager.addSoundElementTypeMedia(PIANO_MUSIC_TWO, "/be/nevies/game/playground/three/sound/classical_piano_music_track_mary_.mp3");
        SoundManager.addSoundElementTypeMedia(PIANO_MUSIC_THREE, "/be/nevies/game/playground/three/sound/classical_piano_music_track_mary_.mp3");
        SoundManager.addSoundElementTypeMedia(PIANO_MUSIC_FOUR, "/be/nevies/game/playground/three/sound/classical_piano_music_track_mary_.mp3");
        SoundManager.addSoundElementTypeMedia(STATIC_NOISE_TV_SOUND, "/be/nevies/game/playground/three/sound/static_noise_from_tv_with_no_signal.mp3");
        SoundManager.addSoundElementTypeAudioClip(CLOCK_SOUND, "/be/nevies/game/playground/three/sound/clock_tick_002.mp3");
        SoundManager.addSoundElementTypeAudioClip(BUTTON_CLICK_SOUND, "/be/nevies/game/playground/three/sound/multimedia_button_click_001.mp3");
        LOG.info("End loading sound");

        SoundMenu menu = new SoundMenu(new VBox());
        //addElementToGameMainNode(menu);
        SoundElement sound = SoundManager.getSoundElement(PIANO_MUSIC_TWO);
        sound.setVolumeDistanceBased(true);
        sound.setBalanceDirectionBased(true);
        sound.setSoundArea(new Rectangle(100, 50, 100, 50));
        sound.showSoundArea();

        SoundElement sound2 = SoundManager.getSoundElement(CLOCK_SOUND);
        sound2.setVolumeDistanceBased(true);
        sound2.setBalanceDirectionBased(true);
        sound2.setSoundArea(new Rectangle(200, 50, 100, 50));
        sound2.showSoundArea();

        SoundElement sound3 = SoundManager.getSoundElement(PIANO_MUSIC_THREE);
        sound3.setVolumeDistanceBased(true);
        sound3.setBalanceDirectionBased(false);
        sound3.setSoundArea(new Rectangle(100, 150, 400, 400));
        sound3.showSoundArea();

        SoundElement sound4 = SoundManager.getSoundElement(PIANO_MUSIC_FOUR);
        sound4.setVolumeDistanceBased(false);
        sound4.setBalanceDirectionBased(false);
        sound4.setSoundArea(new Rectangle(300, 350, 100, 100));
        sound4.showSoundArea();

        addElementToGameMainNode(player);
        player.setLayoutX(80);
        player.setLayoutY(20);

        SoundManager.setMainElement(player);

        getGameMainNode().addEventHandler(GameEvent.GAME_UPDATE_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                // nothing for the moment
            }
        });

        setupInput(getGameScene());
    }

    @Override
    protected void handleSoundUpdate() {
        SoundManager.staticCheckForCollisions();
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

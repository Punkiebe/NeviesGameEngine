/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.three;

import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.sound.SoundElement;
import be.nevies.game.engine.core.sound.SoundManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class SoundMenu extends Element<VBox> {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SoundMenu.class);

    public SoundMenu(VBox node) {
        super(node);

        // Start play music
        Button pianoPlay = ButtonBuilder.create().text("Play piano music.").onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SoundElement pianoMusic = SoundManager.getSoundElement(PlayGroundThree.PIANO_MUSIC);
                pianoMusic.play();
            }
        }).build();
        node.getChildren().add(pianoPlay);

        // Pause play music
        Button pianoPause = ButtonBuilder.create().text("Pause piano music.").onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SoundElement pianoMusic = SoundManager.getSoundElement(PlayGroundThree.PIANO_MUSIC);
                pianoMusic.pause();
            }
        }).build();
        node.getChildren().add(pianoPause);

        // Stop play music
        Button pianoStop = ButtonBuilder.create().text("Stop piano music.").onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SoundElement pianoMusic = SoundManager.getSoundElement(PlayGroundThree.PIANO_MUSIC);
                pianoMusic.stop();
            }
        }).build();
        node.getChildren().add(pianoStop);

        // Volum for music
        Slider volumePiano = SliderBuilder.create().min(0).max(1).value(1).prefWidth(20).build();
        volumePiano.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                SoundElement pianoMusic = SoundManager.getSoundElement(PlayGroundThree.PIANO_MUSIC);
                LOG.debug("Old value {} , new value {}", t, t1);
                pianoMusic.setVolume(t1.doubleValue());
            }
        });
        node.getChildren().add(volumePiano);
        
        // Start button click sound
        Button buttonClickPlay = ButtonBuilder.create().text("Play button click.").onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SoundElement buttonSound = SoundManager.getSoundElement(PlayGroundThree.BUTTON_CLICK_SOUND);
                buttonSound.play(SoundElement.INDEFINITE);
            }
        }).build();
        node.getChildren().add(buttonClickPlay);
        
        // Stop button click sound
        Button buttonClickStop = ButtonBuilder.create().text("Stop button click.").onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SoundElement buttonSound = SoundManager.getSoundElement(PlayGroundThree.BUTTON_CLICK_SOUND);
                buttonSound.stop();
            }
        }).build();
        node.getChildren().add(buttonClickStop);
        
        
        // Start static noise tv
        Button staticNoisePlay = ButtonBuilder.create().text("Play static noise tv.").onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SoundElement sound = SoundManager.getSoundElement(PlayGroundThree.STATIC_NOISE_TV_SOUND);
                sound.play(2);
            }
        }).build();
        node.getChildren().add(staticNoisePlay);
        
        // Stop static noise tv
        Button staticNoiseStop = ButtonBuilder.create().text("Stop static noise tv.").onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                SoundElement sound = SoundManager.getSoundElement(PlayGroundThree.STATIC_NOISE_TV_SOUND);
                sound.stop();
            }
        }).build();
        node.getChildren().add(staticNoiseStop);
    }
}

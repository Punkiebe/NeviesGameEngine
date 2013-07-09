/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.sound;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author drs
 */
public class SoundService extends Service<SoundElement> {

    private SoundElement soundElement;

    public void setSoundElement(SoundElement sound) {
        soundElement = sound;
    }

    public SoundElement getSoundElement() {
        return soundElement;
    }

    @Override
    protected Task<SoundElement> createTask() {
        final SoundElement sound = getSoundElement();
        return new Task<SoundElement>() {
            @Override
            protected SoundElement call() throws Exception {
                if (sound == null) {
                    throw new IllegalAccessException("SoundElement can't be null!! Please set the soundElement before starting the service.");
                }
                sound.play();
                return sound;
            }
        };
    }
}

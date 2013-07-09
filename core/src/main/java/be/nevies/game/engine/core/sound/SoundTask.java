/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.sound;

import javafx.concurrent.Task;

/**
 *
 * @author drs
 */
public class SoundTask extends Task<Void> {
    
    private final SoundElement sound;

    public SoundTask(SoundElement soundElement) {
        sound = soundElement;
    }

    @Override
    protected Void call() throws Exception {
        System.out.println(">> callinging");
        sound.play();
        SoundElement.Status status = SoundElement.Status.PLAYING;
        System.out.println("<> status : " + status);
        while (status == SoundElement.Status.PLAYING) {
            Thread.sleep(100);
            System.out.println("waited");
            status = sound.getStatus();
            System.out.println("<> status : " + status );
        }
        return null;
    }
    
    
}

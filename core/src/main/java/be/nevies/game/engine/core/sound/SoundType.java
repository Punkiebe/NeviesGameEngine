package be.nevies.game.engine.core.sound;

/**
 * This tells what type of sound the SoundElement is.
 * AudioClip should be used for small audio files where you don't need mutch control over, like sound effects.
 * Media should be used for larger audio files where you need a lot of control over, like Music and Dialog.
 * 
 * @author drs
 */
public enum SoundType {
    
    AUDIOCLIP,
    MEDIA
}

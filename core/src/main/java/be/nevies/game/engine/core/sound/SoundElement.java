package be.nevies.game.engine.core.sound;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;

/**
 * This is the general class for all your sound objects. It's abstracts the difference between AudioClips and Media. But you still need to create the
 * SoundElement as one of the two types. So you need to know the difference between the two. Make a SoundElement of type AudioClip for a small sound file, like
 * sound effects. Make a SoundElement of type Media for a larger sound file like music and dialogs.
 *
 * @author drs
 */
public class SoundElement {

    /**
     * When cycleCount is set to this value, the SoundElement will loop continuously until stopped.
     */
    public static final int INDEFINITE = -1;
    private final SoundType soundType;
    private AudioClip soundClip;
    private MediaPlayer soundMedia;
    private DoubleProperty volumeProperty;
    private IntegerProperty cycleCountProperty;
    private DoubleProperty balanceProperty;
    private Rectangle soundArea;
    private boolean volumeDistanceBased;
    private boolean balanceDirectionBased;

    /**
     * Use this constructor if you want to create a SoundElement based on the AudioClip format. Mostly used for smaller audio fragments, like sound effects.
     *
     * @param clip Your AudioClip.
     */
    public SoundElement(AudioClip clip) {
        if (clip == null) {
            throw new IllegalArgumentException("The AudioClip can't be null!!");
        }
        soundClip = clip;
        volumeProperty = soundClip.volumeProperty();
        soundType = SoundType.AUDIOCLIP;
        cycleCountProperty = soundClip.cycleCountProperty();
        balanceProperty = soundClip.balanceProperty();
    }

    /**
     * Use this constructor if you want to create a SoundElement based on the Media format. Mostly used for larger audio fragments, like music and dialogs.
     *
     * @param media Your Media. This we'll be used to create a MediaPlayer object that's used internal.
     */
    public SoundElement(Media media) {
        if (media == null) {
            throw new IllegalArgumentException("The Media can't be null!!");
        }
        soundMedia = new MediaPlayer(media);
        volumeProperty = soundMedia.volumeProperty();
        soundType = SoundType.MEDIA;
        cycleCountProperty = soundMedia.cycleCountProperty();
        balanceProperty = soundMedia.balanceProperty();
    }

    /**
     * Play the selected sound element.
     */
    public void play() {
        switch (soundType) {
            case AUDIOCLIP:
                soundClip.play();
                break;
            case MEDIA:
                soundMedia.play();
                break;
            default:
                throw new IllegalAccessError("You shouldn't be able to call this method if no AudioClip or MediaPlayer is set!!");
        }
    }

    /**
     * Play the selected sound element.
     *
     * @param cycleCount Set the number of times this SoundElement needs to play.
     */
    public void play(int cycleCount) {
        cycleCountProperty.setValue(cycleCount);
        switch (soundType) {
            case AUDIOCLIP:
                soundClip.play();
                break;
            case MEDIA:
                soundMedia.play();
                break;
            default:
                throw new IllegalAccessError("You shouldn't be able to call this method if no AudioClip or MediaPlayer is set!!");
        }
    }

    /**
     * Pause the selected sound element. An AudioClip doesn't have a pause option. This will stop the AudioClip!
     */
    public void pause() {
        switch (soundType) {
            case AUDIOCLIP:
                soundClip.stop();
                break;
            case MEDIA:
                soundMedia.pause();
                break;
            default:
                throw new IllegalAccessError("You shouldn't be able to call this method if no AudioClip or MediaPlayer is set!!");
        }
    }

    /**
     * Stop the selected sound element.
     */
    public void stop() {
        switch (soundType) {
            case AUDIOCLIP:
                soundClip.stop();
                break;
            case MEDIA:
                soundMedia.stop();
                break;
            default:
                throw new IllegalAccessError("You shouldn't be able to call this method if no AudioClip or MediaPlayer is set!!");
        }
    }

    /**
     * @return What type of SoundElement this is, AUDIOCLIP or MEDIA.
     */
    public SoundType getSoundType() {
        return soundType;
    }

    /**
     * Set the volume of the sound element.
     *
     * @param volume The volume at which the media should be played. The range of effective values is [0.0 1.0] where 0.0 is inaudible and 1.0 is full volume,
     * which is the default.
     */
    public void setVolume(double volume) {
        volumeProperty.setValue(volume);
    }

    /**
     * @param balance Set the balance for this sound element. Balance is the relative left and right volume levels of the sound element.
     */
    public void setBalance(double balance) {
        balanceProperty.setValue(balance);
    }
    
    /**
     * @param area The area you want the sound to be played. Set to null if you want to remove the area.
     */
    public void setSoundArea(Rectangle area) {
        soundArea = area;
    }
    
    /**
     * @return True if this sound element has a sound area.
     */
    public boolean hasSoundArea() {
        return soundArea == null ? false : true;
    }
    
    /**
     * @param distanceBased Set true or false if you want that the balance is based on the distance of your main element and the sound area. This can't work without a sound area!
     */
    public void setVolumeDistanceBased(boolean distanceBased) {
        volumeDistanceBased = distanceBased;
    }
    
    /**
     * @return Tells if the volume is balanced around the distance.
     */
    public boolean isVolumeDistanceBased() {
        return volumeDistanceBased;
    }
    
    /**
     * @param directionBased 
     */
    public void setBalanceDirectionBased(boolean directionBased) {
        balanceDirectionBased = directionBased;
    }
    
    /**
     * @return Tells if the balance is based on the direction.
     */
    public boolean isBalanceDirectionBased() {
        return balanceDirectionBased;
    }
}

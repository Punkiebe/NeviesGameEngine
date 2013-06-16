package be.nevies.game.engine.core.sound;

import be.nevies.game.engine.core.general.Element;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public final class SoundManager {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SoundManager.class);
    private static SoundManager instance;
    private Map<String, SoundElement> soundMap;
    private Element mainElement;
    private static boolean checkingCollision = false;

    /**
     * Default constructor.
     */
    private SoundManager() {
        soundMap = new HashMap<>();
    }

    /**
     * @return The single instance of this class.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Add a SoundElement to the map of sounds.
     *
     * @param name The name of the sound element.
     * @param element The SoundElement object.
     */
    public static void addSoundElement(String name, SoundElement element) {
        if (name == null || element == null) {
            throw new IllegalArgumentException("Name and/or element can't be null when adding a SoundElement to the SoundManager!!");
        }
        if (getInstance().soundMap.containsKey(name)) {
            LOG.warn("For the name {} there is already a SoundElement in the map!", name);
            return;
        }
        getInstance().soundMap.put(name, element);
    }

    /**
     * This method we'll create of SoundElement using the AudioClip object and add it to the map of sounds. The AudioClip type is best used for smaller sound
     * files, like sound effects.
     *
     * @param name The name of the sound element.
     * @param path The path of the AudioClip you want to add. It uses the getReource of class.
     */
    public static void addSoundElementTypeAudioClip(String name, String path) {
        try {
            addSoundElement(name, new SoundElement(new AudioClip(SoundManager.class.getResource(path).toURI().toString())));
        } catch (URISyntaxException ex) {
            LOG.error("Problems creating the SoundElement with name '{}' and as path '{}'.", name, path);
        }
    }

    /**
     * This method we'll create of SoundElement using the Media object and add it to the map of sounds. The Media type is best used for larger sound files, like
     * dialogs and music.
     *
     * @param name The name of the sound element.
     * @param path The path of the Media you want to add. It uses the getReource of class.
     */
    public static void addSoundElementTypeMedia(String name, String path) {
        try {
            addSoundElement(name, new SoundElement(new Media(SoundManager.class.getResource(path).toURI().toString())));
        } catch (URISyntaxException ex) {
            LOG.error("Problems creating the SoundElement with name '{}' and as path '{}'.", name, path);
        }
    }

    /**
     * Get a SoundElement based on the name.
     *
     * @param name The name of the sound element.
     * @return The SoundElement or null if there's no sound element for the name.
     */
    public static SoundElement getSoundElement(String name) {
        if (getInstance().soundMap.containsKey(name)) {
            return getInstance().soundMap.get(name);
        }
        return null;
    }

    /**
     * The main element is used to determen from where the sound is hearth from. This need to be set if you want to use the distance based sound option.
     *
     * @param mainElement Your main element.
     */
    public static void setMainElement(Element mainElement) {
        getInstance().mainElement = mainElement;
    }

    /**
     * This will check the main element against all the SoundElements that have a sound area. If there's a collision with a SoundElement the sound we'l be
     * played. And if the 'volumeDistanceBased' is true then it we'll adapt the volume based on the distance oft he main element to the center of the sound
     * area.
     */
    public static void checkForCollisions() {
        if (checkingCollision) {
            LOG.info("Already checking for collisions, this call we'll be ignored!!");
            return;
        }
        checkingCollision = true;
        try {
            // check for collisions
        } catch (RuntimeException rte) {
            LOG.error("There where problems while checken for collisions.", rte);
        } finally {
            checkingCollision = false;
        }
    }
}

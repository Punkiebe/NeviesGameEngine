/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.general.BehaviourType;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.sound.SoundElement;
import be.nevies.game.engine.core.sound.SoundManager;
import be.nevies.game.engine.tiled.plugin.map.PropertiesType;
import be.nevies.game.engine.tiled.plugin.map.PropertyType;
import be.nevies.game.engine.tiled.plugin.util.TiledPluginUtil;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import org.slf4j.LoggerFactory;

/**
 * Extend your PropertiesHandler from this class, then the following things are
 * handled from the core module : <ul> <li>Add Behaviour to your element
 * ('BehaviourClass')</li>
 * <li>Add music to the SoundManager ('MusicSource')</li>
 * <li>Add passive elements to the CollisionManager('PassiveCollision')</li>
 * </ul>
 *
 * @author drs
 */
public class DefaultPropertiesHandler implements PropertiesHandler {

    /* Logger. */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TileLayerCreator.class);

    @Override
    public void handleProperties(Element element, PropertiesType properties) {
        if (properties == null || properties.getProperty() == null || properties.getProperty().size() <= 0) {
            // if no properties given do nothing.
            return;
        }
        if (element == null) {
            LOG.debug("The element was null, given to the handleProperties method.");
        }
        List<PropertyType> propertyTypes = properties.getProperty();
        for (PropertyType property : propertyTypes) {
            if (property.getName() == null || "".equals(property)) {
                LOG.debug("There was a property with an empty name! Has as value : {}.", property.getValue());
                continue;
            }
            switch (property.getName()) {
                case "BehaviourClass":
                    handleBehaviourClass(element, property, properties);
                    break;
                case "PassiveCollision":
                    handlePassiveCollision(element, property);
                    break;
                case "MusicSource":
                    handleMusicSource(element, property, properties);
                    break;
            }
        }
    }

    /**
     * @param element This element is used as the SoundArea for this music.
     * Should be a class that extends 'Rectangle'!!
     * @param property The property with key 'MusicSource'.
     * @param properties The other properties of this element. Could hold one of
     * the following properties: 'MusicVolume', 'VolumeDistanceBased',
     * 'BalanceDirectionBased'.
     */
    private void handleMusicSource(Element element, PropertyType property, PropertiesType properties) {
        if (element == null) {
            LOG.warn("The element can't be null to handle the MusicSource.");
            return;
        }
        String sourceStr = property.getValue();
        LOG.trace("Handle name 'MusicSource' with value '{}'.", sourceStr);
        if (sourceStr == null || "".equals(sourceStr)) {
            return;
        }
        URL source = DefaultPropertiesHandler.class.getResource(sourceStr);
        String path = null;
        try {
            path = source.toURI().getPath();
        } catch (URISyntaxException ex) {
            LOG.error("Errors while getting the path for the music source.", ex);
        }
        AudioClip audio = new AudioClip(source.toExternalForm());
        //AudioClip audio = new AudioClip("jar:" + sourceStr);

        SoundElement soundElement = new SoundElement(audio);

        // Set default values
        soundElement.setVolume(1.0);
        soundElement.setBalance(1.0);
        soundElement.setBalanceDirectionBased(false);
        soundElement.setVolumeDistanceBased(false);

        soundElement.setSoundArea((Rectangle) element.getNode());
        soundElement.showSoundArea();
        
        if (Boolean.TRUE.equals(TiledPluginUtil.getBooleanValueForKeyFromProperties(properties, "VolumeDistanceBased"))) {
            soundElement.setVolumeDistanceBased(true);
        }

        if (Boolean.TRUE.equals(TiledPluginUtil.getBooleanValueForKeyFromProperties(properties, "BalanceDistanceBased"))) {
            soundElement.setBalanceDirectionBased(true);
        }

        Double volume = TiledPluginUtil.getDoubleValueForKeyFromProperties(properties, "MusicVolume");
        if (volume != null) {
            soundElement.setVolume(volume / 100);
        }

        SoundManager.addSoundElement(element.getId(), soundElement);
    }

    /**
     * @param element The element where you want to add the behaviour to.
     * @param property The property with key 'BehaviourClass'.
     * @param properties The other properties of this element. Should have a
     * property with key 'BehaviourTypes'.
     */
    private void handleBehaviourClass(Element element, PropertyType property, PropertiesType properties) {
        if (element == null) {
            LOG.warn("The element can't be null to handle the BehaviourClass.");
            return;
        }
        String classStr = property.getValue();
        LOG.trace("Handle name 'BehaviourClass' with value '{}'.", classStr);
        if (classStr == null || "".equals(classStr)) {
            return;
        }
        try {
            Class<?> behaviourClass = null;
            behaviourClass = ClassLoader.getSystemClassLoader().loadClass(classStr);
            if (!BehaviourType.class.isAssignableFrom(behaviourClass)) {
                LOG.error("Your Behaviour class should extend the BehaviourType class!!");
                return;
            }
            String behaviourTypes = TiledPluginUtil.getValueForKeyFromProperties(properties, "BehaviourTypes");
            String[] types = behaviourTypes.split(",");
            for (String type : types) {
                try {
                    Field field = behaviourClass.getField(type);
                    element.addBehaviour(field.getName());
                    LOG.trace("Added the Behaviour : {} , to the element : {}.", field.getName(), element);
                } catch (NoSuchFieldException | SecurityException ex) {
                    LOG.warn("Couldn't find the Field : {} from the BaseClass : {}", type, behaviourClass);
                }
            }
        } catch (ClassNotFoundException ex) {
            LOG.warn("Couldn't load the handler : {}", classStr);
        }
    }

    /**
     * Adds the element to the passiveElement list of the CollisionManager. The
     * collision bounds are set also, to the local bounds of the element.
     *
     * @param element The element.
     * @param property The property with name 'PassiveCollision'.
     */
    private void handlePassiveCollision(Element element, PropertyType property) {
        if (element == null) {
            LOG.warn("The element can't be null to handle the PassiveCollision.");
            return;
        }
        if ("true".equalsIgnoreCase(property.getValue())) {
            element.addCollisionBounds(element.getBoundsInLocal());
            CollisionManager.addPassiveElement(element);
            LOG.trace("Added following element as a passive element to the CollisionManager : {}", element);
        }
    }
}

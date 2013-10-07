/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.general.BehaviourType;
import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.tiled.plugin.map.PropertiesType;
import be.nevies.game.engine.tiled.plugin.map.PropertyType;
import be.nevies.game.engine.tiled.plugin.util.TiledPluginUtil;
import java.lang.reflect.Field;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 * Extend your PropertiesHandler from this class, then the following things are handled from the core module :
 * <ul>
 * <li>Adding Behaviour to your element</li>
 * <li>Adding your element to the CollisionManager(passive and active)</li>
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
            }
        }
    }

    private void handleBehaviourClass(Element element, PropertyType property, PropertiesType properties) {
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
     * Adds the element to the passiveElement list of the CollisionManager.
     * The collision bounds are set also, to the local bounds of the element.
     * 
     * @param element The element.
     * @param property The property with name 'PassiveCollision'.
     */
    private void handlePassiveCollision(Element element, PropertyType property) {
        if ("true".equalsIgnoreCase(property.getValue())) {
            element.addCollisionBounds(element.getBoundsInLocal());
            CollisionManager.addPassiveElement(element);
            LOG.trace("Added following element as a passive element to the CollisionManager : {}", element);
        }
    }
}

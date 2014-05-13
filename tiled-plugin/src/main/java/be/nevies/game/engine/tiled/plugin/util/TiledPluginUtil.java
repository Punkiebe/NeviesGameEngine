/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.util;

import be.nevies.game.engine.tiled.plugin.map.PropertiesType;
import be.nevies.game.engine.tiled.plugin.map.PropertyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An util class for this plugin.
 *
 * @author drs
 *
 * @since 1.0.0
 */
public class TiledPluginUtil {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(TiledPluginUtil.class);

    /**
     * Get the value for the given key from the properties.
     * 
     * @param properties THe PropertiesType object that holds the properties.
     * @param key The key. It uses the 'equalsIgnoreCase'.
     * @return The value for the key. It returns null if it was not found, or if one of the method arguments where null.
     */
    public static String getValueForKeyFromProperties(PropertiesType properties, String key) {
        if (properties == null) {
            LOG.warn("You tried to get the value from an PropertiesType object that is null!!");
            return null;
        }
        if (key == null || "".equals(key)) {
            LOG.warn("You tried to get value from an empty key!!");
            return null;
        }
        for (PropertyType prop : properties.getProperty()) {
            if (key.equalsIgnoreCase(prop.getName())) {
                return prop.getValue();
            }
        }
        return null;
    }

    /**
     * Get the value as boolean for the given key from the properties.
     * 
     * @param properties THe PropertiesType object that holds the properties.
     * @param key The key. It uses the 'equalsIgnoreCase'.
     * @return The value for the key. It returns null if it was not found, or if one of the method arguments where null, or if the value wasn't a boolean.
     */
    public static Boolean getBooleanValueForKeyFromProperties(PropertiesType properties, String key) {
        if (properties == null) {
            LOG.warn("You tried to get the value from an PropertiesType object that is null!!");
            return null;
        }
        if (key == null || "".equals(key)) {
            LOG.warn("You tried to get value from an empty key!!");
            return null;
        }
        for (PropertyType prop : properties.getProperty()) {
            if (key.equalsIgnoreCase(prop.getName())) {
                String value = prop.getValue();
                if ("true".equalsIgnoreCase(value)) {
                    return true;
                } else if ("false".equalsIgnoreCase(value)) {
                    return false;
                }
                LOG.warn("The value '{}' for the key '{}' wasn't 'true' or 'false'!", value, key);
                return null;
            }
        }
        return null;
    }
}

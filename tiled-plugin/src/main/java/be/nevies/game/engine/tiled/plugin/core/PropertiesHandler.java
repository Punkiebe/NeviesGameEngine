/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.tiled.plugin.map.PropertiesType;

/**
 * The interface for your properties handler.
 * 
 * @author drs
 * 
 * @since 1.0.0
 */
public interface PropertiesHandler {
 
    void handleProperties(Element element, PropertiesType properties);
}

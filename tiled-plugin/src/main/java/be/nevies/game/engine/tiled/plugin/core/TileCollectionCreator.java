/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.graphic.TileCollection;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.map.Tileset;
import java.util.List;

/**
 *
 * @author drs
 */
public class TileCollectionCreator {
    
    public TileCollection createTileSetFromMap(Map map) {
        List<Tileset> tileset = map.getTileset();
        return null;
    }
}

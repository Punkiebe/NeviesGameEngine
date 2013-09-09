/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.graphic.TileCollection;
import be.nevies.game.engine.tiled.plugin.map.DataType;
import be.nevies.game.engine.tiled.plugin.map.DataType;
import be.nevies.game.engine.tiled.plugin.map.LayerType;
import be.nevies.game.engine.tiled.plugin.map.LayerType;
import be.nevies.game.engine.tiled.plugin.map.Map;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Group;

/**
 * 
 * @author drs
 */
public class TileLayerCreator {
    
    public static Group createLayersForMap(Map map) {
        Group mapGroup = new Group();
        Collection<TileCollection> tileCollection = TileCollectionCreator.createTileCollectionsFromMap(map);
        List<Serializable> layerOrObjectgroupOrImagelayer = map.getLayerOrObjectgroupOrImagelayer();
        Iterator<Serializable> iterator = layerOrObjectgroupOrImagelayer.iterator();
        for (Serializable serializable : layerOrObjectgroupOrImagelayer) {
            if (serializable instanceof LayerType) {
                LayerType layer = (LayerType) serializable;
                Group layerGroup = createLayer(tileCollection, layer, map.getTilewidth(), map.getTileheight());
                layerGroup.setId(layer.getName());
                mapGroup.getChildren().add(layerGroup);
            }
        }
        
        return mapGroup;
    }
    
    public static Group createLayer(Collection<TileCollection> tileCollection, LayerType layer, long tileWidth, long tileHeight) {
        Group layerGroup = new Group();
        DataType data = layer.getData();
        System.out.println(">> data value : " + data.getContent());
        
        return layerGroup;
    }
}

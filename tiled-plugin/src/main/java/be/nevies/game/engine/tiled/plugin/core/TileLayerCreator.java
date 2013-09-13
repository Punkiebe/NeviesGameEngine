/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.graphic.Sprite;
import be.nevies.game.engine.core.graphic.TileCollection;
import be.nevies.game.engine.tiled.plugin.map.DataType;
import be.nevies.game.engine.tiled.plugin.map.DataType;
import be.nevies.game.engine.tiled.plugin.map.LayerType;
import be.nevies.game.engine.tiled.plugin.map.LayerType;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.map.TileLayerType;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Group;
import javax.xml.bind.JAXBElement;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class TileLayerCreator {
    
    /* Logger. */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TileLayerCreator.class);

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
        if (data.getContent().isEmpty()) {
            return layerGroup;
        }

        // We asume here that if there's only one content, that's it's the encoded string
        if (data.getContent().size() == 1) {
            List<Serializable> content = layer.getData().getContent();
            Serializable next = content.iterator().next();
            if (next instanceof String) {
                // TODO complete 
                throw new UnsupportedOperationException("Not yet supported for encoded data xmls.");
            }
            return layerGroup;
        }

        long totalHorTiles = layer.getWidth() / tileWidth;
        long totalVerTiles =  layer.getHeight() / tileHeight;
        long countHorTiles = 0;
        long countVerTiles = 0;
        
        List<Serializable> content = layer.getData().getContent();
        for (Serializable con : content) {
            System.out.println(">> cont class : " + con.getClass());
            if (con instanceof JAXBElement) {
                JAXBElement jaxbEl = (JAXBElement) con;

                if (jaxbEl.getValue() instanceof TileLayerType) {
                    TileLayerType tileLayer = (TileLayerType) jaxbEl.getValue();
                    System.out.println(">> tileLayer " + tileLayer.getGid());
                    long x = countHorTiles * tileWidth;
                    long y = countVerTiles * tileHeight;
                    countHorTiles++;
                    if (countHorTiles == totalHorTiles) {
                        countHorTiles = 0;
                        countVerTiles++;
                    }
                    
                    if (tileLayer.getGid() != 0) {
                        // Retrieve the tile
                        Sprite tile = retrieveTileUsingGid(tileLayer.getGid(), tileCollection);
                        tile.setLayoutX(x);
                        tile.setLayoutY(y);
                        layerGroup.getChildren().add(tile);
                    }
                }
            }
        }
        
        if (countVerTiles == totalVerTiles && countHorTiles == 0) {
            // All was oké
            LOG.debug("The creation of the layer was correct");
        } else {
            LOG.warn("There where isseus with the creation of the tile layer. totalVer : {}, totalHor : {}, countVer : {}, countHor : {}", totalVerTiles, totalHorTiles, countVerTiles, countHorTiles);
        }

        return layerGroup;
    }

    /**
     * @param gid The gid ID.
     * @param tileCollection Collection of all TileCollections.
     * @return The Sprite object representing the gid.
     */
    private static Sprite retrieveTileUsingGid(Long gid, Collection<TileCollection> tileCollection) {
        for (TileCollection tileCol : tileCollection) {
            Sprite sprite = tileCol.getSprite(TileCollectionCreator.PREFIX_TILE_NAME + gid);
            if (sprite != null) {
                return sprite;
            }
        }
        return null;
    }
}

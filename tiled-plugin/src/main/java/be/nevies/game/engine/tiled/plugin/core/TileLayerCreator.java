/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.general.Element;
import be.nevies.game.engine.core.graphic.Sprite;
import be.nevies.game.engine.core.graphic.TileCollection;
import be.nevies.game.engine.tiled.plugin.map.DataType;
import be.nevies.game.engine.tiled.plugin.map.LayerType;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.map.ObjectType;
import be.nevies.game.engine.tiled.plugin.map.ObjectgroupType;
import be.nevies.game.engine.tiled.plugin.map.PropertiesType;
import be.nevies.game.engine.tiled.plugin.map.PropertyType;
import be.nevies.game.engine.tiled.plugin.map.TileLayerType;
import be.nevies.game.engine.tiled.plugin.type.RectangleShapeElement;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javax.xml.bind.JAXBElement;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class TileLayerCreator {

    /* Logger. */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TileLayerCreator.class);

    /**
     * Use this method to load your tmx map in to your game.
     * Add the group to the scene to see your map in game.
     * 
     * @param map The tmx Map object.
     * @param tmxFile The file object that corresponds with the tmx map.
     * @return A group node that holds all the layers and object groups that are present in the map.
     */
    public static Group createGroupFromMap(Map map, File tmxFile) {
        Group mapGroup = new Group();

        Collection<TileCollection> tileCollection = TileCollectionCreator.createTileCollectionsFromMap(map, tmxFile);
        List<Serializable> layerOrObjectgroupOrImagelayer = map.getLayerOrObjectgroupOrImagelayer();
        Iterator<Serializable> iterator = layerOrObjectgroupOrImagelayer.iterator();
        for (Serializable serializable : layerOrObjectgroupOrImagelayer) {
            if (serializable instanceof LayerType) {
                LayerType layer = (LayerType) serializable;
                Group layerGroup = createLayer(tileCollection, layer, map.getTilewidth(), map.getTileheight());
                if (layerGroup != null) {
                    layerGroup.setId(layer.getName());
                    mapGroup.getChildren().add(layerGroup);
                }
            } else if (serializable instanceof ObjectgroupType) {
                ObjectgroupType objGroup = (ObjectgroupType) serializable;
                Group objectGroup = createObjectGroup(objGroup);
                if (objectGroup != null) {
                    objectGroup.setId(objGroup.getName());
                    mapGroup.getChildren().add(objectGroup);
                }
            }
        }

        return mapGroup;
    }

    /**
     * @param objGroup The ObjectGroups from the map.
     * @return A group node that represents the different objects defined in the ObjectGroup.
     */
    public static Group createObjectGroup(ObjectgroupType objGroup) {
        Group group = new Group();
        PropertiesType properties = objGroup.getProperties();
        String baseClassStr = getValueForKeyFromProperties(properties, "BaseClass");
        Class<?> baseClass = null;
        if (baseClassStr != null && !"".equals(baseClassStr)) {
            try {
                baseClass = ClassLoader.getSystemClassLoader().loadClass(baseClassStr);
            } catch (ClassNotFoundException ex) {
                LOG.warn("Couldn't load the BaseClass : {}", baseClassStr);
                return null;
            }
        }
        for (ObjectType objectType : objGroup.getObject()) {
            Element el = null;
            if (objectType.getEllipse() != null) {
                // TODO
            } else if (objectType.getPolygon() != null) {
                // TODO
            } else if (objectType.getPolyline() != null) {
                // TODO
            } else if (objectType.getImage() != null) {
                // TODO
            } else {
                // normal
                el = new RectangleShapeElement(new Rectangle(objectType.getX(), objectType.getY(), objectType.getWidth(), objectType.getHeight()));
            }
            if (el == null) {
                LOG.info("Couldn't  create an element for object type : {}", objectType.getName());
                continue;
            }
            el.setId(objectType.getName());
            el.addCollisionBounds(el.getBoundsInLocal());
            if (baseClass != null) {
                try {
                    Field field = baseClass.getField(objectType.getType());
                    el.addBehaviour(field.getName());
                } catch (NoSuchFieldException | SecurityException ex) {
                    LOG.warn("Couldn't find the Field : {} from the BaseClass : {}", objectType.getType(), baseClass);
                }
            }
            group.getChildren().add(el);
        }
        return group;
    }

    /**
     * This creates a group with sprite's that corresponds with given layer.
     * 
     * @param tileCollection The TileCollections used for this layerType.
     * @param layer The LayerType to use to create the group.
     * @param tileWidth Tile width.
     * @param tileHeight Tile height.
     * @return The group with sprite's that corresponds with given layer.
     */
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

        long totalHorTiles = layer.getWidth();
        long totalVerTiles = layer.getHeight();
        long countHorTiles = 0;
        long countVerTiles = 0;
        LOG.debug("totalHorTiles : {} , totalVerTiles : {}", totalHorTiles, totalVerTiles);
        List<Serializable> content = layer.getData().getContent();
        for (Serializable con : content) {
            if (con instanceof JAXBElement) {
                JAXBElement jaxbEl = (JAXBElement) con;

                if (jaxbEl.getValue() instanceof TileLayerType) {
                    TileLayerType tileLayer = (TileLayerType) jaxbEl.getValue();
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

    /**
     * @param properties THe PropertiesType object that holds the properties.
     * @param key The key. It uses the 'equalsIgnoreCase'.
     * @return The value for the key. It returns null if it was not found, or if one of the method arguments where null.
     */
    private static String getValueForKeyFromProperties(PropertiesType properties, String key) {
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
}

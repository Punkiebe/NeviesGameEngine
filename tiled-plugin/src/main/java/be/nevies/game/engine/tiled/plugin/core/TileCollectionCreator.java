/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.graphic.TileCollection;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.map.TilesetType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 *
 * @author drs
 */
public class TileCollectionCreator {

    public static final String PREFIX_TILE_NAME = "TILED_";

    /**
     * Reads the list of Tileset's of the map, and creates a collection of TileCollection's to use for building your layers.
     * 
     * @param map The map object you want to create TileCollections from.
     * @return A collection of all TileCollections that are used in this map.
     */
    public static Collection<TileCollection> createTileCollectionsFromMap(Map map) {
        Collection<TileCollection> tileCols = new ArrayList<>();
        List<TilesetType> tilesets = map.getTileset();
        for (TilesetType tileset : tilesets) {
            Image sourceImage = retrieveSourceImage(tileset);
            TileCollection tileCol = new TileCollection(tileset.getName(), sourceImage);
            fillUpTileCollectionWithTiles(tileset, tileCol, (int) sourceImage.getHeight(), (int) sourceImage.getWidth());
            System.out.println(">> tile col : " + tileCol.toString());
            tileCols.add(tileCol);
        }
        return tileCols;
    }

    /**
     * @param The Tileset object from the map.
     * @return The Image object that goes with the tileset.
     */
    private static Image retrieveSourceImage(TilesetType tileset) {
        String source = null;
        if (tileset.getSource() == null || "".equals(tileset.getSource())) {
            List<be.nevies.game.engine.tiled.plugin.map.ImageType> image = tileset.getImage();
            if (image.size() > 1) {
                throw new IllegalArgumentException("We don't support mutiple images in one tileset!");
            }
            be.nevies.game.engine.tiled.plugin.map.ImageType next = image.iterator().next();
            source = next.getSource();
        } else {
            source = tileset.getSource();
        }
        if (source == null || "".equals(source)) {
            throw new IllegalArgumentException("You need a source for your image before you can create a tile collection.");
        }

        return new Image(source);
    }

    /**
     * @param tileset The tileset from the map.
     * @param tilecol The tile collection you want the tiles from the Tileset to be added to.
     * @param imageHeight The image height.
     * @param imageWidth The image width.
     */
    private static void fillUpTileCollectionWithTiles(TilesetType tileset, TileCollection tilecol, int imageHeight, int imageWidth) {
        long tileheight = tileset.getTileheight();
        long tilewidth = tileset.getTilewidth();
        long firstgid = tileset.getFirstgid();
        long gid = firstgid;
        long heightIter = imageHeight / tileheight;
        long widthIter = imageWidth / tilewidth;
        for (int i = 0; i < heightIter; i++) {
            for (int j = 0; j < widthIter; j++) {
                long x = 0 + (tilewidth * j);
                long y = 0 + (tileheight * i);
                tilecol.defineOneTile(PREFIX_TILE_NAME + gid, new Rectangle2D(x, y, tilewidth, tileheight));
                gid++;
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.graphic.TileSet;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 * TileSet mapper class for the image : "be/nevies/game/playground/one/images/free_tileset_version_9.png".
 * 
 * @author drs
 */
public class FreeTileSetVersion9Mapper extends TileSet {

    public static final String TILE_GROUND_TYPE_1 = "TILE_GROUND_TYPE_1";
    public static final String TILE_GROUND_TYPE_2 = "TILE_GROUND_TYPE_2";
    public static final String TILE_GROUND_TYPE_3 = "TILE_GROUND_TYPE_3";
    public static final String TILE_GROUND_TYPE_4 = "TILE_GROUND_TYPE_4";

    public FreeTileSetVersion9Mapper() {
        super("FreeTileSerVersion9", new Image("be/nevies/game/playground/one/images/free_tileset_version_9.png"));
        initMap();
    }

    private void initMap() {
        defineOneTile(TILE_GROUND_TYPE_1, new Rectangle2D(64, 0, 32, 32));
        defineOneTile(TILE_GROUND_TYPE_2, new Rectangle2D(96, 0, 32, 32));
        defineOneTile(TILE_GROUND_TYPE_3, new Rectangle2D(128, 0, 32, 32));
        defineOneTile(TILE_GROUND_TYPE_4, new Rectangle2D(160, 0, 32, 32));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

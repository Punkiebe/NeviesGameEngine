package be.nevies.game.playground.four;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.sound.SoundManager;
import be.nevies.game.engine.tiled.plugin.core.TileLayerCreator;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class PlayGroundFour extends GameController {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(PlayGroundFour.class);
    private PlayerFour player = new PlayerFour();

    public PlayGroundFour(int gups, int sups, String title) {
        super(gups, sups, title);
    }

    @Override
    public void initialise() {
        LOG.debug("Start loading map");
        SoundManager.initialise(getGameMainNode());
        URL resource = ReadTmxFile.class.getResource("/be/nevies/game/engine/tiled/plugin/example/firstHouseXML.tmx");
        File file = null;
        try {
            file = new File(resource.toURI());
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(PlayGroundFour.class.getName()).log(Level.SEVERE, null, ex);
        }
        ReadTmxFile read = new ReadTmxFile(file);
        Map map = read.getMapFromTmxFile();
        Group layer = TileLayerCreator.createGroupFromMap(map, file);

        getGameMainNode().getChildren().add(layer);
        LOG.debug("End loading map");
        player.setLayoutX(120);
        player.setLayoutY(90);
        //player.addBehaviour(PlayGroundFourBehaviour.NOT_CROSSABLE);
        getGameMainNode().getChildren().add(player);
        CollisionManager.addActiveElement(player);
        SoundManager.setMainElement(player);
        Button button1 = ButtonBuilder.create()
                .text("Print active elements.")
                .onAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                CollisionManager.printActiveElements();
            }
        }).build();

        Label labelActive = new Label("Print active elements");
        labelActive.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event t) {
                CollisionManager.printActiveElements();
            }
        });
        labelActive.setTextFill(Color.WHITE);

        Label labelPassive = new Label("Print passive elements");
        labelPassive.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event t) {
                CollisionManager.printPassiveElements();
            }
        });
        labelPassive.setTextFill(Color.WHITE);

        Label labelLastCheck = new Label("Print resut map last check");
        labelLastCheck.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event t) {
                CollisionManager.printContentResultMapLastCheck();
            }
        });
        labelLastCheck.setTextFill(Color.WHITE);

        labelLastCheck.setSkin(new ButtonSkin<>(labelLastCheck));

        VBox menuVbox = VBoxBuilder.create()
                .spacing(5)
                .translateX(400)
                .translateY(20)
                .children(labelActive, labelPassive, labelLastCheck)
                .build();

        MenuBox menuBox = new MenuBox(menuVbox);
        addElementToGameMainNode(menuBox);
    }

    @Override
    protected void handleGameUpdate() {
        CollisionManager.staticCheckForCollisions();
    }

    @Override
    protected void handleSoundUpdate() {
        SoundManager.staticCheckForCollisions();
    }

    @Override
    public void defineSceneEvents(Scene scene) {
        EventHandler movePlayerEvent = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                LOG.trace("Scene event : {}", t);
                switch (t.getCode()) {
                    case UP:
                        player.moveUp();
                        break;
                    case DOWN:
                        player.moveDown();
                        break;
                    case LEFT:
                        player.moveLeft();
                        break;
                    case RIGHT:
                        player.moveRight();
                        break;
                }
                //     t.consume();
            }
        };
        scene.setOnKeyPressed(movePlayerEvent);
    }
}

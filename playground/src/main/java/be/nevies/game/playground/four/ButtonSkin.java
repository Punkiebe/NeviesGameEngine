/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.four;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author drs
 */
public class ButtonSkin<C extends Label> implements Skin<C> {

    private C skinnable;

    public ButtonSkin(C skinnable) {
        this.skinnable = skinnable;
    }

    @Override
    public Node getNode() {
        StackPane stack = new StackPane();
        Text text = new Text(skinnable.getText());
        text.setManaged(true);
        Rectangle back = new Rectangle(200,20,Color.BLUE);
        back.setManaged(true);
       // text.setStyle("-fx-background-color: red;");
        stack.getChildren().addAll(back, text);
        stack.setCursor(Cursor.HAND);
        stack.setAlignment(Pos.TOP_LEFT);
        return stack;
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public C getSkinnable() {
        return skinnable;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.core.animation;

import be.nevies.game.engine.core.general.Element;
import javafx.scene.Group;
import javafx.scene.shape.Path;

/**
 *
 * @author drs
 */
public class PathElement extends Element<Path> {

    public PathElement(Path node) {
        super(node);
    }

    public PathElement(Path node, Group group) {
        super(node, group);
    }

    public void addPathElement(javafx.scene.shape.PathElement element) {
        getNode().getElements().add(element);
    }
}

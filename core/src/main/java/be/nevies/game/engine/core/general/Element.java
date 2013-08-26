package be.nevies.game.engine.core.general;

import be.nevies.game.engine.core.event.GameEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is games element class. Each type of 'element' in your game should extend from this class. Your element needs a generic type that extends Node. The
 * Element class extends of type Parent. So the object Element is a Node on his own also.
 *
 * @author drs
 */
public abstract class Element<T extends Node> extends Parent {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(Element.class);

    /* Node object. */
    private T node;
    private Collection<Rectangle> collisionBounds;
    private BooleanProperty remove = new SimpleBooleanProperty(false);
    private Collection<String> behaviourTypes;
    private Group groupOfElement;

    /**
     * Default constructor. Adds the given node to the children of this element. This adds an EventHandler for the GameEvent COLLISION_EVENT, the handler calls
     * the method 'handleCollision'. Also a listener is added for the 'remove' boolean property, the method 'handleRemoveElement' is called.
     *
     * @param node An object that's of the generic type this element is constructed with. Can't be null!!
     */
    public Element(T node) {
        if (node == null) {
            throw new IllegalArgumentException("The given node was null!!");
        }
        this.node = node;
        this.getChildren().add(node);
        this.collisionBounds = new ArrayList<>();
        this.behaviourTypes = new HashSet();
        this.addEventHandler(GameEvent.COLLISION_EVENT, new EventHandler<GameEvent>() {
            @Override
            public void handle(GameEvent t) {
                LOG.debug("Handle collision event for, source : {}, target : {}", t.getGameEventObject().getSource(), t.getGameEventObject().getTarget());
                handleCollision(t);
            }
        });
        this.remove.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                handleRemoveElement();
            }
        });
    }

    /**
     * With this constructor you add the node straight to the give group. First the default constructor is called.
     *
     * @param node An object that's of the generic type this element is constructed with. Can't be null!!
     * @param group The group you want the node to be added to. Can't be null!! else use the default constructor.
     */
    public Element(T node, Group group) {
        this(node);
        if (group != null) {
            group.getChildren().add(this);
            groupOfElement = group;
        }
    }

    /**
     * @return Node you give to the constructor.
     */
    public T getNode() {
        return node;
    }

    /**
     * Use this method if you want to move this element!
     *
     * @param x The amount you want to move on the x-axis.
     */
    public void moveX(double x) {
        setLayoutX(getLayoutX() + x);
    }

    /**
     * Use this method if you want to move this element!
     *
     * @param y THe amount you want to move on the y-axis;
     */
    public void moveY(double y) {
        setLayoutY(getLayoutY() + y);
    }

    /**
     * Add as many bounds as you want to the collection of collision bounds.
     *
     * @param bounds Add bounds to the collision collection.
     */
    public void addCollisionBounds(Bounds... bounds) {
        for (Bounds bound : bounds) {
            Rectangle rectangle = new Rectangle(bound.getMinX(), bound.getMinY(), bound.getWidth(), bound.getHeight());
            rectangle.setVisible(false);
            getChildren().add(rectangle);
            collisionBounds.add(rectangle);
        }
    }

    /**
     * Removes the bounds that you want to remove from the collection of collision bounds.
     *
     * @param bounds The bounds that need to be removed from the collision collection.
     */
    public void removeCollisionBounds(Bounds... bounds) {
        // @TODO refactor this!!
        collisionBounds.removeAll(Arrays.asList(bounds));
    }

    /**
     * @param group The group you want to add the element to.
     * @throws IllegalAccessException Is thrown when you try to add the Element to a group, when it has already a group.
     */
    public void addElementToGroup(Group group) throws IllegalAccessException {
        if (groupOfElement != null) {
            throw new IllegalAccessException("Can't add the element to a group. It already assigned to a group.");
        }
        groupOfElement = group;
        groupOfElement.getChildren().add(this);
    }

    /**
     * This removes this element from its current group. The 'remove' property isn't set!!
     *
     * @return If the element was removed from group or not.
     */
    public boolean removeElementFromGroup() {
        if (groupOfElement == null) {
            return false;
        }
        return groupOfElement.getChildren().remove(this);
    }

    /**
     * This gives the transformed collision bounds.
     *
     * @return A collection of collision bounds.
     */
    public Collection<Rectangle> getCollisionBounds() {
        Transform transform = getLocalToSceneTransform();
        Collection<Rectangle> updated = new ArrayList<>();
        for (Rectangle rectangle : collisionBounds) {
            Rectangle update = new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            update.getTransforms().add(transform);
            updated.add(update);
        }
        return updated;
    }

    /**
     * @return True if there are collision bounds for this element.
     */
    public boolean hasCollisionBounds() {
        return !collisionBounds.isEmpty();
    }

    /**
     * Override this method if you want to handle a collision event to this element.
     *
     * @param event The GameEvent object that holds the info about the collision event.
     */
    protected void handleCollision(GameEvent event) {
        // Implement if you need to handle a collision for your element
    }

    /**
     * Override this method if you want to execute something when this element is selected to be removed.
     */
    protected void handleRemoveElement() {
        // Implement if you need something special when removing the element
    }

    /**
     * Call this method if you want to show the collision bounds.
     */
    public void showCollisionBounds() {
        for (Rectangle bounds : collisionBounds) {
            bounds.setVisible(true);
            bounds.setOpacity(0.5);
            bounds.setFill(Color.RED);
        }
    }

    /**
     * Call this method if you want to hide the collision bounds.
     */
    public void hideCollisionBounds() {
        for (Rectangle bounds : collisionBounds) {
            bounds.setVisible(false);
        }
    }

    /**
     * @param behaviour The behaviour you want to add to this element.
     */
    public void addBehaviour(String... behaviour) {
        behaviourTypes.addAll(Arrays.asList(behaviour));
    }

    /**
     *
     * @param behaviour The behaviour you want to remove from this element.
     */
    public void removeBehaviour(String... behaviour) {
        behaviourTypes.removeAll(Arrays.asList(behaviour));
    }

    /**
     * @return A collection of behaviour types for this Element.
     */
    public Collection<String> getBehaviourTypes() {
        return Collections.unmodifiableCollection(behaviourTypes);
    }

    /**
     * @return True if the element has behaviour.
     */
    public boolean hasBehaviourTypes() {
        return !behaviourTypes.isEmpty();
    }

    /**
     * Check if a behaviour type is present in the element.
     *
     * @param behaviour The behaviour type you want to check.
     * @return True if the behaviour is present for this element.
     */
    public boolean checkForBehaviour(String behaviour) {
        if (hasBehaviourTypes()) {
            for (String type : getBehaviourTypes()) {
                if (type.equals(behaviour)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets the remove property to true, so the 'handleRemoveElement' method we'll be triggered. It also remove the element from a group if it has a group.
     */
    public void removeElement() {
        remove.set(true);
        if (groupOfElement != null) {
            groupOfElement.getChildren().remove(this);
        }
    }
}

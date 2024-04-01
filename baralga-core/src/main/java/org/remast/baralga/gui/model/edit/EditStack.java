/**
 *
 */
package org.remast.baralga.gui.model.edit;

import com.google.common.eventbus.Subscribe;
import org.remast.baralga.gui.actions.RedoAction;
import org.remast.baralga.gui.actions.UndoAction;
import org.remast.baralga.gui.events.BaralgaEvent;
import org.remast.baralga.gui.model.PresentationModel;
import org.remast.baralga.gui.model.UndoRedoManager;
import org.remast.baralga.model.ProjectActivity;

import java.util.List;
import java.util.Stack;

/**
 * Edit stack for undoing and redoing edit actions. The stack observes
 * the model and keeps track of undoable and redoable events.
 * @author remast
 */
public class EditStack {

    /**
     * The action for undoing an edit activity.
     */
    private UndoAction undoAction;

    /**
     * The action for redoing an edit activity.
     */
    private RedoAction redoAction;

    /**
     * The undoable edit events.
     */
    private final Stack<BaralgaEvent> undoStack = new Stack<>();

    /**
     * The redoable edit events.
     */
    private final Stack<BaralgaEvent> redoStack = new Stack<>();

    /** The model. */
    private PresentationModel model;

    /**Utility type to manage undo/redo*/
    private UndoRedoManager undoRedoManager;


    /**
     * Creates a new edit stack for the given model.
     * @param model the edited model to create stack for
     */
    public EditStack(final PresentationModel model) {
        this.model = model;
        this.undoAction = new UndoAction(this);
        this.redoAction = new RedoAction(this);
        this.undoRedoManager = new UndoRedoManager();
        updateActions();
    }

    @Subscribe
    public void update(final Object eventObject) {
        if (!(eventObject instanceof BaralgaEvent)) {
            return;
        }

        final BaralgaEvent event = (BaralgaEvent) eventObject;

        // Ignore our own events
        if (this == event.getSource()) {
            return;
        }

        if (event.canBeUndone()) {
            undoStack.push(event);
            updateActions();
        }
    }

    /**
     * Enable or disable actions.
     */
    private void updateActions() {
        if (!undoStack.isEmpty()) {
            undoAction.setEnabled(true);
            undoAction.setText(undoStack.peek().getUndoText());
        } else {
            undoAction.setEnabled(false);
            undoAction.resetText();
        }

        if (!redoStack.isEmpty()) {
            redoAction.setEnabled(true);
            redoAction.setText(redoStack.peek().getRedoText());
        } else {
            redoAction.setEnabled(false);
            redoAction.resetText();
        }
    }

    /**
     * @return the undoAction
     */
    public final UndoAction getUndoAction() {
        return undoAction;
    }

    /**
     * @return the redoAction
     */
    public final RedoAction getRedoAction() {
        return redoAction;
    }

    /**
     * Undo last edit action.
     */
    public void undo() {
        undoRedoManager.undo();
        updateActions();
    }
    /**
     * Redo last edit action.
     */
    public void redo() {
        undoRedoManager.redo();
        updateActions();
    }

}

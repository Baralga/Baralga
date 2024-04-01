package org.remast.baralga.gui.model;

import org.remast.baralga.gui.actions.IUndoRedoAction;

import java.util.Stack;

public class UndoRedoManager {
    private Stack<IUndoRedoAction> undoStack = new Stack<>();
    private Stack<IUndoRedoAction> redoStack = new Stack<>();

    public void performAction(IUndoRedoAction action) {
        action.perform();
        undoStack.push(action);
        redoStack.clear(); // Clear redo stack on new action to avoid redoing unrelated actions
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            IUndoRedoAction action = undoStack.pop();
            action.undo();
            redoStack.push(action);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            IUndoRedoAction action = redoStack.pop();
            action.perform();
            undoStack.push(action);
        }
    }
}

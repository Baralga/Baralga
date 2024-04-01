package org.remast.baralga.gui.actions;

public interface IUndoRedoAction {
    void perform(); // Perform the action, making changes to the model.
    void undo();    // Undo the action, reverting changes made to the model.
}

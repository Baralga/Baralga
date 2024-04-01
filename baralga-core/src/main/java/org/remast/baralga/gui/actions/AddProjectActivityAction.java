package org.remast.baralga.gui.actions;

import org.remast.baralga.gui.model.PresentationModel;
import org.remast.baralga.model.ProjectActivity;

public class AddProjectActivityAction implements IUndoRedoAction {
    private PresentationModel model;
    private ProjectActivity activity;

    public AddProjectActivityAction(PresentationModel model, ProjectActivity activity) {
        this.model = model;
        this.activity = activity;
    }

    @Override
    public void perform() {
        // Assuming addActivity is a method in PresentationModel
        model.addActivity(activity, this);
    }

    @Override
    public void undo() {
        // Assuming removeActivity is a method in PresentationModel
        model.removeActivity(activity, this);
    }
}

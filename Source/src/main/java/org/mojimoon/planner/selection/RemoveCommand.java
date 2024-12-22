package org.mojimoon.planner.selection;

import org.mojimoon.planner.model.Attraction;

public class RemoveCommand extends Command {
    private String date;
    private Attraction attraction;

    public boolean execute(String date, String name) {
        this.date = date;
        this.attraction = FindAttraction.find(Selected.getSelected().get(date), name);

        if (Selected.remove(date, attraction)) {
            addUndo(this);
            clearRedo();
            return true;
        } else {
            return false;
        }
    }

    public void undo() {
        Selected.add(date, attraction);
    }

    public void redo() {
        Selected.remove(date, attraction);
    }
}
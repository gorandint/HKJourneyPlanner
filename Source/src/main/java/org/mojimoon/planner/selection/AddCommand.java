package org.mojimoon.planner.selection;

import org.mojimoon.planner.model.Attraction;

public class AddCommand extends Command {
    private String date;
    private Attraction attraction;

    public boolean execute(String date, String name) {
        this.date = date;
        this.attraction = FindAttraction.find(name);

        if (Selected.add(date, attraction)) {
            addUndo(this);
            clearRedo();
            return true;
        } else {
            return false;
        }
    }

    public void undo() {
        Selected.remove(date, attraction);
    }

    public void redo() {
        Selected.add(date, attraction);
    }
}
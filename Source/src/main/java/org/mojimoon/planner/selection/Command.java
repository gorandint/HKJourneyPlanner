package org.mojimoon.planner.selection;

import java.util.Stack;

public abstract class Command {
    private static final Stack<Command> undoStack = new Stack<>();
    private static final Stack<Command> redoStack = new Stack<>();
    
    public abstract boolean execute(String date, String name);

    public abstract void undo();

    public abstract void redo();

    protected static void addUndo(Command cmd) {
        undoStack.push(cmd);
    }

    protected static void clearRedo() {
        redoStack.clear();
    }

    public static boolean undoPrev() {
        if (undoStack.isEmpty()) {
            return false;
        }
        Command cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
        return true;
    }

    public static boolean redoPrev() {
        if (redoStack.isEmpty()) {
            return false;
        }
        Command cmd = redoStack.pop();
        cmd.redo();
        undoStack.push(cmd);
        return true;
    }

    // for test only
    public static void resetState() {
        undoStack.clear();
        redoStack.clear();
    }
}
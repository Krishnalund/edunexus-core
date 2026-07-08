package org.example;

import java.util.EmptyStackException;
import java.util.Stack;

public class UndoRedoManager {
    private Stack<Action> undoStack;
    private Stack<Action> redoStack;

    // Constructor
    public UndoRedoManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    // Perform and record a new action
    public void performAction(Action action) {
        System.out.println("Performing action: " + action.getActionType());
        undoStack.push(action);
        redoStack.clear();
    }

    // Undo last action
    public void undoLastAction() throws EmptyStackException {
        if (undoStack.isEmpty()) {
            throw new EmptyStackException();
        }
        Action lastAction = undoStack.pop();
        try {
            lastAction.reverse();
            redoStack.push(lastAction);
        } catch (Exception e) {
            System.err.println("Undo failed: " + e.getMessage());
        }
    }

    // Redo last undone action
    public void redoAction() throws EmptyStackException {
        if (redoStack.isEmpty()) {
            throw new EmptyStackException();
        }
        Action redoAction = redoStack.pop();
        System.out.println("Re-executing action: " + redoAction.getActionType());
        undoStack.push(redoAction);
    }
}

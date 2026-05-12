package commands.meta;

import shared.elements.Route;

public interface Undoable {
    public void undo(Route... routes);
    public void redo(Route... routes);
}

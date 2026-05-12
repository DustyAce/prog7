package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Invoker;
import commands.meta.Undoable;
import shared.elements.Route;
import handlers.CollectionHandler;

public class RemoveGreaterCommand implements Command, Undoable {
    @Override
    public String desc() {
        return "remove all shared.elements greater than input";
    }

    @Override
    public void execute(CommandArgs ca) {
        Route[] r = CollectionHandler.remove_greater(ca.route());
        Invoker.addToRouteHistory(r);
    }

    @Override
    public void undo(Route... routes) {
        CollectionHandler.add(routes, false);
    }

    @Override
    public void redo(Route... routes) {
        if (routes.length == 1) {
            CollectionHandler.remove_greater(routes[0]);
        }
    }

    @Override
    public String getName() {
        return "remove_greater";
    }
}

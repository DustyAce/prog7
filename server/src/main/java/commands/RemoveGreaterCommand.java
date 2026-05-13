package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import commands.meta.Undoable;
import handlers.DatabaseHandler;
import shared.elements.Route;
import handlers.CollectionHandler;
import shared.requests.CommandRequest;

import java.util.HashSet;

public class RemoveGreaterCommand implements Command, Undoable {
    @Override
    public String desc() {
        return "remove all elements greater than input";
    }

    @Override
    public void execute(CommandRequest cr) {
        HashSet<Long> idsToRemove = DatabaseHandler.removeGreater(cr.getRoute(), cr.getUsername());
        Route[] r = CollectionHandler.clear(idsToRemove);
        Invoker.addToRouteHistory(r);
    }

    @Override
    public void undo(Route... routes) {
        CollectionHandler.add(routes, false);
    }

    @Override
    public void redo(Route... routes) {
//        todo
//        if (routes.length == 1) {
//            CollectionHandler.remove_greater(routes[0]);
//        }
    }

    @Override
    public String getName() {
        return "remove_greater";
    }
}

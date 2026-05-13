package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import commands.meta.Undoable;
import handlers.DatabaseHandler;
import shared.elements.Route;
import handlers.CollectionHandler;
import shared.requests.CommandRequest;

import java.util.Arrays;
import java.util.HashSet;

public class ClearCommand implements Command, Undoable {
    @Override
    public String desc() {
        return "clear all data";
    }

    @Override
    public void execute(CommandRequest cr) {
        HashSet<Long> idsToRemove = DatabaseHandler.clear(cr.getUsername());
        Route[] r = CollectionHandler.clear(idsToRemove);
        Invoker.addToRouteHistory(r);
    }

    @Override
    public void undo(Route... routes) {
        HashSet<Route> hashRoutes = new HashSet<>(Arrays.asList(routes));
        CollectionHandler.setRoutes(hashRoutes);
    }

    @Override
    public void redo(Route... routes) {
        //todo
        //CollectionHandler.clear();
    }

    @Override
    public String getName() {
        return "clear";
    }
}

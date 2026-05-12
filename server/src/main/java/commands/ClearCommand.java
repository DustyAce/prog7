package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Invoker;
import commands.meta.Undoable;
import shared.elements.Route;
import handlers.CollectionHandler;

import java.util.Arrays;
import java.util.HashSet;

public class ClearCommand implements Command, Undoable {
    @Override
    public String desc() {
        return "clear all data";
    }

    @Override
    public void execute(CommandArgs ca) {
        Route[] r = CollectionHandler.clear();
        Invoker.addToRouteHistory(r);
    }

    @Override
    public void undo(Route... routes) {
        HashSet<Route> hashRoutes = new HashSet<>(Arrays.asList(routes));
        CollectionHandler.setRoutes(hashRoutes);
    }

    @Override
    public void redo(Route... routes) {
        CollectionHandler.clear();
    }

    @Override
    public String getName() {
        return "clear";
    }
}

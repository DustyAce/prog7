package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Invoker;
import commands.meta.Undoable;
import shared.elements.Route;
import handlers.CollectionHandler;

public class AddMinCommand implements Command, Undoable {
    @Override
    public String desc() {
        return "add a Route if it's less than all the other Routes";
    }

    @Override
    public void execute(CommandArgs ca) {
        Route r = ca.route();
        if (CollectionHandler.add_if_min(r)) {
            Invoker.addToRouteHistory(r);
        }
    }

    @Override
    public void undo(Route... routes) {
        if (routes.length == 1 && routes[0] != null) {
            CollectionHandler.remove_by_id(routes[0].getId());
        }
    }

    @Override
    public void redo(Route... routes) {
        if (routes.length == 1 && routes[0] != null) {
            CollectionHandler.add(routes[0]);
        }
    }

    @Override
    public String getName() {
        return "add_if_min";
    }
}

package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import commands.meta.Undoable;
import handlers.DatabaseHandler;
import shared.elements.Route;
import handlers.CollectionHandler;
import shared.requests.CommandRequest;

public class AddMinCommand implements Command, Undoable {
    @Override
    public String desc() {
        return "add a Route if it's less than all the other Routes";
    }

    @Override
    public void execute(CommandRequest cr) {
        Route r = cr.getRoute();
        if (DatabaseHandler.checkIfMin(r)){
            Long id = DatabaseHandler.insert(r, cr.getUsername());
            if (id != null) {
                r.setId(id);
                CollectionHandler.add(r);
                Invoker.addToRouteHistory(r);
            }

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

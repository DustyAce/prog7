package commands;

import commands.meta.Command;
import commands.meta.Invoker;
import commands.meta.Undoable;
import handlers.DatabaseHandler;
import shared.elements.Route;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

public class UpdateCommand implements Command, Undoable {
    @Override
    public String desc() {
        return "update an element with {id}";
    }

    @Override
    public void execute(CommandRequest cr) {
        try {
            if (DatabaseHandler.update(cr.getRoute(), cr.getUsername() )) {
                CollectionHandler.update(cr.getRoute());
                Invoker.addToRouteHistory(cr.getRoute());
                OutputHandler.message("Successfully updated.");
            }
        } catch (NumberFormatException e) {
            OutputHandler.message("Invalid id");
        }
    }

    @Override
    public void undo(Route... routes) {
        Route oldRoute = routes[0];
        CollectionHandler.removeById(oldRoute.getId());
        CollectionHandler.add(oldRoute);
    }

    @Override
    public void redo(Route... routes) {
        Route newRoute = routes[1];
        CollectionHandler.removeById(newRoute.getId());
        CollectionHandler.add(newRoute);
    }

    @Override
    public String getName() {
        return "update";
    }
}

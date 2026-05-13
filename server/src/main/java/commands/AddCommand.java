package commands;
import commands.meta.Command;
import commands.meta.Invoker;
import commands.meta.Undoable;
import handlers.DatabaseHandler;
import shared.elements.Route;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

public class AddCommand implements Command, Undoable {
    public String desc() {return "add an object";}
    public void execute(CommandRequest cr) {
        Route r = cr.getRoute();
        Long id = DatabaseHandler.insert(r, cr.getUsername());
        if (id != null) {
            r.setId(id);
            CollectionHandler.add(r);
            Invoker.addToRouteHistory(r);
        }

    }

    @Override
    public void undo(Route... routes) {
        if (routes.length == 1) {
            CollectionHandler.remove_by_id(routes[0].getId());
        } else {
            OutputHandler.message("Undo failed, bad argument");
        }
    }

    @Override
    public void redo(Route... routes) {
        if (routes.length == 1) {
            CollectionHandler.add(routes[0]);
        } else {
            OutputHandler.message("Redo failed, bad argument");
        }
    }

    @Override
    public String getName() {
        return "add";
    }
}

package commands;
import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Invoker;
import commands.meta.Undoable;
import shared.elements.Route;
import handlers.CollectionHandler;
import handlers.OutputHandler;

public class AddCommand implements Command, Undoable {
    public String desc() {return "add an object";}
    public void execute(CommandArgs ca) {
        Route r = ca.route();
        CollectionHandler.add(r);
        Invoker.addToRouteHistory(r);
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

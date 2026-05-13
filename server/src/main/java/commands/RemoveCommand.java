package commands;

import commands.meta.Command;
import commands.meta.Undoable;
import handlers.DatabaseHandler;
import shared.elements.Route;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

public class RemoveCommand implements Command, Undoable {
    public String desc() { return "removes Route with specified {id}"; }

    public void execute(CommandRequest cr) {
        long id;
        try {
            id = Long.parseLong(cr.getArgs()[0]);
        } catch (NumberFormatException e) {
            OutputHandler.message("Bad argument!");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
        OutputHandler.message("Provide a Route id number!");
        return;
        }
        if (DatabaseHandler.remove(id, cr.getUsername())) {
            CollectionHandler.remove_by_id(id);
        }

    }

    @Override
    public void undo(Route... routes) {
        if (routes.length == 1) {
            CollectionHandler.add(routes, false);
        } else { OutputHandler.message("rm.undo failed, bad argument");}
    }

    @Override
    public void redo(Route... routes) {
        if (routes.length == 1) {
            CollectionHandler.remove_by_id(routes[0].getId());
        } else { OutputHandler.message("rm.undo failed, bad argument");}
    }

    @Override
    public String getName() {
        return "remove";
    }
}

package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import commands.meta.Undoable;
import shared.elements.Route;
import handlers.CollectionHandler;
import handlers.OutputHandler;

public class RemoveCommand implements Command, Undoable {
    public String desc() { return "removes Route with specified {id}"; }

    public void execute(CommandArgs ca) {
        long id;
        try {
            id = Long.parseLong(ca.args()[0]);
        } catch (NumberFormatException e) {
            OutputHandler.message("Bad argument!");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
        OutputHandler.message("Provide a Route id number!");
        return;
        }
        CollectionHandler.remove_by_id(id);
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

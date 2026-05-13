package commands;

import commands.meta.Command;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

public class MoreCommand implements Command {
    public String desc() {
        return "Route info with specified {id}";
    }

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
        CollectionHandler.more(id);
    }

    @Override
    public String getName() {
        return "more";
    }
}

package commands;

import commands.meta.Command;
import handlers.CollectionHandler;
import shared.requests.CommandRequest;

public class InfoCommand implements Command {
    @Override
    public String desc() {
        return "output info about the HashSet";
    }

    @Override
    public void execute(CommandRequest cr) {
        CollectionHandler.info();
    }

    @Override
    public String getName() {
        return "info";
    }
}

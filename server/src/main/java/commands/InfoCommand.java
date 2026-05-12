package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import handlers.CollectionHandler;

public class InfoCommand implements Command {
    @Override
    public String desc() {
        return "output info about the HashSet";
    }

    @Override
    public void execute(CommandArgs ca) {
        CollectionHandler.info();
    }

    @Override
    public String getName() {
        return "info";
    }
}

package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import handlers.FileHandler;

public class LoadCommand implements Command {
    @Override
    public String desc() {
        return "load collection from the save file";
    }

    @Override
    public void execute(CommandArgs ca) {
        FileHandler.load();
    }

    @Override
    public String getName() {
        return "load";
    }
}

package commands;

import commands.meta.Command;
import handlers.FileHandler;
import shared.requests.CommandRequest;

public class LoadCommand implements Command {
    @Override
    public String desc() {
        return "load collection from the save file";
    }

    @Override
    public void execute(CommandRequest cr) {
        FileHandler.load();
    }

    @Override
    public String getName() {
        return "load";
    }
}

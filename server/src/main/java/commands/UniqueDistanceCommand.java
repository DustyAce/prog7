package commands;

import commands.meta.Command;
import handlers.CollectionHandler;
import shared.requests.CommandRequest;

public class UniqueDistanceCommand implements Command {
    @Override
    public String desc() {
        return "output a list of unique distance values";
    }

    @Override
    public void execute(CommandRequest cr) {
        CollectionHandler.print_unique_distance();
    }

    @Override
    public String getName() {
        return "print_unique_distance";
    }
}

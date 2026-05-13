package commands;

import commands.meta.Command;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.elements.Route;
import shared.requests.CommandRequest;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PrintAscendingCommand implements Command {
    @Override
    public String desc() {
        return "outputs a sorted list of all current items";
    }

    @Override
    public void execute(CommandRequest cr) {
        OutputHandler.setRoutes(
                CollectionHandler.getRoutes().stream()
                        .sorted(Route::compareTo)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }

    @Override
    public String getName() {
        return "print_ascending";
    }
}

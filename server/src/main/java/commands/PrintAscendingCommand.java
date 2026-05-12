package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.elements.Route;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PrintAscendingCommand implements Command {
    @Override
    public String desc() {
        return "outputs a sorted list of all current items";
    }

    @Override
    public void execute(CommandArgs ca) {
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

package commands;

import commands.meta.Command;
import commands.meta.CommandArgs;
import communication.CommunicationHandler;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.elements.Route;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ShowCommand implements Command {
    public String desc() {return "show the contents of the HashMap";}

    public void execute(CommandArgs ca) {
        OutputHandler.setRoutes( CollectionHandler.getRoutes().stream()
                .sorted(Route::compareTo)
                .collect(Collectors
                        .toCollection(ArrayList::new)));
    }

    @Override
    public String getName() {
        return "show";
    }
}

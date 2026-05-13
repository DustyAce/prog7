package commands;

import commands.meta.Command;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import shared.requests.CommandRequest;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilterNameCommand implements Command {
    @Override
    public String desc() {
        return "output all Routes w/ names containing {pattern}";
    }

    @Override
    public void execute(CommandRequest cr) {
        OutputHandler.setRoutes(
                CollectionHandler.getRoutes().stream()
                        .filter( r -> r.getName()
                                .contains(
                                        String.join( " ", cr.getArgs() )
                        )).collect(Collectors.toCollection(ArrayList::new))
        );
    }

    @Override
    public String getName() {
        return "filter_contains_name";
    }
}
